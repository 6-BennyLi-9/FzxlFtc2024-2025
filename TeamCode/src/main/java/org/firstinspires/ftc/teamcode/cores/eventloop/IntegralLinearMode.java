package org.firstinspires.ftc.teamcode.cores.eventloop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;

import org.acmerobotics.roadrunner.SampleMecanumDrive;
import org.betastudio.ftc.action.Actions;
import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.ui.client.implementation.BaseMapClient;
import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.client.UpdateConfig;
import org.betastudio.ftc.ui.dashboard.DashTelemetry;
import org.betastudio.ftc.ui.log.FtcLogTunnel;
import org.betastudio.ftc.time.Timer;
import org.firstinspires.ftc.teamcode.cores.UtilsMng;
import org.firstinspires.ftc.teamcode.cores.structure.SimpleDriveOp;
import org.firstinspires.ftc.teamcode.CoreDatabase;
import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.betastudio.ftc.RunMode;

import java.util.Locale;
import java.util.Objects;

public abstract class IntegralLinearMode extends LinearOpMode implements IntegralOpMode, Interfaces.ThreadEx {
	public    SampleMecanumDrive drive;
	public    Client             client;
	public    UtilsMng           utils;
	public    Timer              timer;
	protected boolean            is_terminate_method_called;
	protected Exception          inlineUncaughtException;

	@Override
	public void runOpMode() throws InterruptedException {
		FtcLogTunnel.saveAndClear();
		Global.runMode = RunMode.AUTONOMOUS;
		Global.prepareCoreThreadPool();
		Global.currentOpmode = this;
		Global.client = client;
		HardwareDatabase.sync(hardwareMap, true);

		drive = new SampleMecanumDrive(hardwareMap);
		telemetry = new DashTelemetry(FtcDashboard.getInstance(), telemetry);
		telemetry.setAutoClear(true);
		client = new BaseMapClient(telemetry);
		client.setUpdateConfig(UpdateConfig.AUTOMATIC);
		utils = new UtilsMng();
		timer = new Timer();

		Global.service.execute(getLinearThread());
		client.putLine(">>>ROBOT READY!");
		FtcLogTunnel.MAIN.report("Op inline initialized");

		waitForStart();

		client.deleteLine(">>>ROBOT READY!");

		if (! opModeIsActive()) return;
		timer.restart();
		FtcLogTunnel.MAIN.report("Op inline started");

		//		Global.threadManager.add("autonomous-exception-interrupter",new AutonomousMonitor(this::opModeIsActive));

		while (opModeIsActive() && ! is_terminate_method_called) {
			if (null != inlineUncaughtException) {
				if (inlineUncaughtException instanceof OpModeManagerImpl.ForceStopException) {
					closeTask();
				} else {
					FtcLogTunnel.MAIN.report(inlineUncaughtException);
					FtcLogTunnel.MAIN.save(String.format(Locale.SIMPLIFIED_CHINESE, "%tc", System.currentTimeMillis()));
					throw new RuntimeException(inlineUncaughtException);
				}
			}
		}

		preTerminate();
		sendTerminateSignal(is_terminate_method_called ? TerminateReason.NATURALLY_SHUT_DOWN : TerminateReason.USER_ACTIONS);
	}

	public abstract Thread getLinearThread();

	/**
	 * 在用户停止操作时完成
	 */
	public void preTerminate() {
	}

	public void angleCalibration(final double angle) {
		angleCalibration(angle, drive.getPoseEstimate());
	}

	public void angleCalibration(final double target, final Pose2d poseEst) {
		Actions.runAction(() -> {
			final double allowErr = 5, ang = HardwareDatabase.imu.getAngularOrientation().firstAngle;

			final TelemetryPacket p = new TelemetryPacket();
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
		sendTerminateSignal(TerminateReason.NATURALLY_SHUT_DOWN);
	}

	@Override
	public void sendTerminateSignal(final TerminateReason reason, final Exception e) {
		timer.stop();
		CoreDatabase.writeInVals(this, reason, timer.getDeltaTime() * 1.0e-3);
		Global.runMode = RunMode.TERMINATE;
		if (TerminateReason.UNCAUGHT_EXCEPTION == Objects.requireNonNull(reason)) {
			inlineUncaughtException = e;
		} else {
			FtcLogTunnel.MAIN.report("Op terminated by " + reason.name());
			FtcLogTunnel.MAIN.save(String.format(Locale.SIMPLIFIED_CHINESE, "%tc", System.currentTimeMillis()));
			terminateOpModeNow();
		}
	}

	@Override
	public void closeTask() {
		is_terminate_method_called = true;
	}
}
