package org.firstinspires.ftc.teamcode.util.ops;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.action.Actions;
import org.firstinspires.ftc.teamcode.client.Client;
import org.firstinspires.ftc.teamcode.client.DashTelemetry;
import org.firstinspires.ftc.teamcode.client.TelemetryClient;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.structure.SimpleDriveOp;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.util.Timer;
import org.firstinspires.ftc.teamcode.util.UtilMng;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("UnusedReturnValue")
public abstract class IntegralAutonomous extends LinearOpMode {
	private final Map<String, Trajectory> trajectoryMap = new HashMap<>();
	private final Map<String, TrajectorySequence> trajectorySequenceMap = new HashMap<>();
	public SampleMecanumDrive drive;
	public Client client;
	public UtilMng utils;
	public Timer timer;

	@Override
	public final void runOpMode() throws InterruptedException {
		HardwareConstants.sync(hardwareMap, true);
		drive = new SampleMecanumDrive(hardwareMap);
		telemetry = new DashTelemetry(FtcDashboard.getInstance(),telemetry);
		client = new Client(telemetry);
		utils = new UtilMng();
		timer = new Timer();
		initialize();

		TelemetryClient.getInstance().addLine(">>>ROBOT READY!");

		waitForStart();

		TelemetryClient.getInstance().deleteLine(">>>ROBOT READY!");

		if (!opModeIsActive()) return;
		timer.restart();
		final Thread linear = new Thread(this::linear);
		linear.start();
		while (opModeIsActive() && !linear.isInterrupted()) {
			sleep(10);
		}
		linear.interrupt();
		client.interrupt();
	}

	public abstract void initialize();

	public abstract void linear();

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
			final double allowErr = 5,ang=HardwareConstants.imu.getAngularOrientation().firstAngle;
			if(Math.abs(target-ang)<Math.abs(360-target+ang)) {
				if (ang > target + allowErr) {
					Actions.runAction(SimpleDriveOp.build(0, 0, - 0.5));
					return true;
				} else if (ang < target - allowErr) {
					Actions.runAction(SimpleDriveOp.build(0, 0, 0.5));
					return true;
				}
			}else{
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
		timer.stop();
		TelemetryClient.getInstance()
				.changeData("time used", timer.getDeltaTime() * 1.0e-3)
				.changeData("time left", 30 - timer.getDeltaTime() * 1.0e-3);
	}
}
