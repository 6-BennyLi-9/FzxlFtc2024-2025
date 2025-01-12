package org.firstinspires.ftc.cores.eventloop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.acmerobotics.roadrunner.SampleMecanumDrive;
import org.acmerobotics.roadrunner.trajectorysequence.TrajectorySequence;
import org.acmerobotics.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import org.betastudio.ftc.action.Actions;
import org.betastudio.ftc.client.Client;
import org.betastudio.ftc.client.DashTelemetry;
import org.betastudio.ftc.client.TelemetryClient;
import org.firstinspires.ftc.cores.UtilMng;
import org.firstinspires.ftc.cores.structure.SimpleDriveOp;
import org.firstinspires.ftc.teamcode.CoreDatabase;
import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.RunMode;
import org.firstinspires.ftc.teamcode.Timer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("UnusedReturnValue")
public abstract class IntegralAutonomous extends LinearOpMode implements IntegralOpMode {
	private final Map <String, Trajectory>         trajectoryMap           = new HashMap <>();
	private final Map <String, TrajectorySequence> trajectorySequenceMap   = new HashMap <>();
	public        SampleMecanumDrive               drive;
	public        Client                           client;
	public        UtilMng                          utils;
	public        Timer                            timer;
	private       Exception                        inlineUncaughtException = null;

	@Override
	public final void runOpMode() throws InterruptedException {
		Global.runMode = RunMode.autonomous;
		Global.prepareCoreThreadPool();
		Global.currentOpmode = this;
		HardwareDatabase.sync(hardwareMap, true);

		drive = new SampleMecanumDrive(hardwareMap);
		telemetry = new DashTelemetry(FtcDashboard.getInstance(), telemetry);
		client = new TelemetryClient(telemetry);
		client.setAutoUpdate(true);
		utils = new UtilMng();
		timer = new Timer();
		initialize();

		client.addLine(">>>ROBOT READY!");

		waitForStart();

		client.deleteLine(">>>ROBOT READY!");

		if (! opModeIsActive()) return;
		timer.restart();

		//		Global.threadManager.add("autonomous-exception-interrupter",new AutonomousMonitor(this::opModeIsActive));
		Global.threadManager.add("linear", new Thread(this::linear));

		while (opModeIsActive()) {
			if (inlineUncaughtException != null) {
				throw new RuntimeException(inlineUncaughtException);
			}
		}

		preTerminate();
		sendTerminateSignal(TerminateReason.UserActions);
	}

	public abstract void initialize();

	public abstract void linear();

	/**
	 * 在用户停止操作时完成
	 */
	public void preTerminate() {
	}

	public Pose2d registerTrajectory(final String tag, final Trajectory argument) {
		trajectoryMap.put(tag, argument);
		return argument.end();
	}

	public Pose2d registerTrajectory(final String tag, final TrajectorySequence argument) {
		trajectorySequenceMap.put(tag, argument);
		return argument.end();
	}

	public void runTrajectory(final String tag) {
		if (trajectoryMap.containsKey(tag)) {
			drive.followTrajectory(trajectoryMap.get(tag));
		} else {
			drive.followTrajectorySequence(trajectorySequenceMap.get(tag));
		}
	}

	public TrajectoryBuilder generateBuilder(final Pose2d pose) {
		return drive.trajectoryBuilder(pose);
	}

	public TrajectorySequenceBuilder generateSequenceBuilder(final Pose2d pose) {
		return drive.trajectorySequenceBuilder(pose);
	}

	public void angleCalibration(final double angle) {
		angleCalibration(angle, drive.getPoseEstimate());
	}

	public void angleCalibration(final double target, final Pose2d poseEst) {
		Actions.runAction(() -> {
			final double allowErr = 5, ang = HardwareDatabase.imu.getAngularOrientation().firstAngle;

			TelemetryPacket p = new TelemetryPacket();
			p.put("ang", ang);
			p.put("err", Math.abs(target - ang));
			FtcDashboard.getInstance().sendTelemetryPacket(p);

			if (Math.abs(target - ang) < Math.abs(360 - target + ang)) {
				if (ang > target + allowErr) {
					Actions.runAction(SimpleDriveOp.build(0, 0, - 0.5));
					return true;
				} else if (ang < target - allowErr) {
					Actions.runAction(SimpleDriveOp.build(0, 0, 0.5));
					return true;
				}
			} else {
				if (ang > target + allowErr) {
					Actions.runAction(SimpleDriveOp.build(0, 0, 0.5));
					return true;
				} else if (ang < target - allowErr) {
					Actions.runAction(SimpleDriveOp.build(0, 0, - 0.5));
					return true;
				}
			}
			Actions.runAction(SimpleDriveOp.build(0, 0, 0));
			return false;
		});
		drive.setPoseEstimate(poseEst);
	}

	public void flagging_op_complete() {
		sendTerminateSignal(TerminateReason.NaturallyShutDown);
	}

	@Override
	public void sendTerminateSignal(TerminateReason reason) {
		sendTerminateSignal(reason, new NullPointerException("UnModified"));
	}

	@Override
	public void sendTerminateSignal(TerminateReason reason, Exception e) {
		timer.stop();
		CoreDatabase.writeInVals(this, reason, timer.getDeltaTime() * 1.0e-3);
		Global.runMode = RunMode.terminated;
		if (Objects.requireNonNull(reason) == TerminateReason.UncaughtException) {
			inlineUncaughtException = e;
		} else {
			terminateOpModeNow();
		}
	}
}
