package org.firstinspires.ftc.teamcode.eventloop.integral;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.acmerobotics.roadrunner.SampleMecanumDrive;
import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.RunMode;
import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.builder.ActionBuilder;
import org.betastudio.ftc.action.builder.LinkedActionBuilder;
import org.betastudio.ftc.action.utils.AssembledAction;
import org.betastudio.ftc.action.utils.LinkedAction;
import org.betastudio.ftc.action.utils.SleepingAction;
import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.client.UpdateConfig;
import org.betastudio.ftc.ui.client.implementation.BaseMapClient;
import org.betastudio.ftc.ui.dashboard.DashTelemetry;
import org.betastudio.ftc.ui.log.FtcLogTunnel;
import org.betastudio.ftc.util.ExceptionsUtil;
import org.betastudio.ftc.util.Timer;
import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.Hardwares;
import org.firstinspires.ftc.teamcode.eventloop.OverclockOpMode;
import org.firstinspires.ftc.teamcode.eventloop.TerminateReason;
import org.firstinspires.ftc.teamcode.eventloop.trajectory.HeadingTrajectoryBuilder;
import org.firstinspires.ftc.teamcode.eventloop.trajectory.TrajectoryAction;
import org.firstinspires.ftc.teamcode.manager.UtilsMng;

import java.util.Locale;
import java.util.Objects;

public abstract class ActionBasedAutonomous extends OverclockOpMode implements IntegralOpMode, Interfaces.ThreadEx {
	public static final String                   LOW_TPS_WARNING = "⚠警告⚠ TPS偏低！ ⚠警告⚠";
	public              SampleMecanumDrive       drive;
	public              UtilsMng                 utils;
	public              Timer                    timer;
	public              Client                   client;
	public              ActionBuilder            builder;
	public              Runnable                 runner;
	protected           boolean                  is_terminate_method_called;
	protected           HeadingTrajectoryBuilder track;
	private             Throwable                inlineUncaughtException;
	private             Action                   action;

	public abstract void actionBuildEntry();

	@Override
	public void op_init() {
		FtcLogTunnel.saveAndClear();
		Global.currentOpmode = this;
		Global.registerGamepad(gamepad1, gamepad2);
		Global.prepareCoreThreadPool();
		RunMode.globalRunMode = RunMode.AUTONOMOUS;
		Global.client = client;
		timer = new Timer();
		builder = new LinkedActionBuilder();

		telemetry = new DashTelemetry(FtcDashboard.getInstance(), telemetry);
		telemetry.setAutoClear(true);
		telemetry.clearAll();
		client = new BaseMapClient(telemetry);
		client.setUpdateConfig(UpdateConfig.MANUALLY);

		Hardwares.sync(hardwareMap, true);
		Hardwares.chassisConfig();
		utils = new UtilsMng();

		telemetry.clearAll();

		drive = new SampleMecanumDrive(hardwareMap);
		track = new HeadingTrajectoryBuilder(drive);
		TrajectoryAction.setClient(client);

		client.putData("TPS", "wait for start");
		client.putData("time", "wait for start");
		client.putLine("ROBOT INITIALIZE COMPLETE!");
		client.putLine("=======================");

		FtcLogTunnel.MAIN.report("Op inline initialized");

		drive.setPoseEstimate(getInitialPose());
		track.setCurrent(getInitialPose());
		actionBuildEntry();
		action = builder.store();
		runner = () -> {
			if (! action.activate()) {
				client.putLine("Core Action Finished");
				runner = () -> {};
			}
		};
	}

	@Override
	public void loop_init() {
		client.changeData("TPS", (1.0e3 / timer.restartAndGetDeltaTime()) + "(not started)");
		client.update();
	}

	@Override
	public void op_start() {
		FtcLogTunnel.MAIN.report("Op inline started successfully");
	}

	@Override
	public void op_loop() {
		double tps = 1.0e3 / timer.restartAndGetDeltaTime();
		client.changeData("TPS", tps);

		if (null != inlineUncaughtException) {
			FtcLogTunnel.MAIN.report(inlineUncaughtException);
			Throwable cause = ExceptionsUtil.getOriginException(inlineUncaughtException);
			throw new RuntimeException(cause);
		}

		if (is_terminate_method_called) {
			op_end();
			terminateOpModeNow();
		}

		drive.update();
		runner.run();

		checkTPS(tps);
		client.update();
	}

	protected void checkTPS(double tps) {
		if (tps < 30) {
			client.putLine(LOW_TPS_WARNING);
		} else {
			client.deleteLine(LOW_TPS_WARNING);
		}
	}

	@Override
	public void op_end() {
		client.clear();

		RunMode.globalRunMode = RunMode.TERMINATE;

		if (null != inlineUncaughtException) {
			Throwable cause = ExceptionsUtil.getOriginException(inlineUncaughtException);
			FtcLogTunnel.MAIN.report(cause);
			throw new RuntimeException(cause);
		}

		FtcLogTunnel.MAIN.report("Op inline closed");
		FtcLogTunnel.MAIN.save(String.format(Locale.SIMPLIFIED_CHINESE, "%tc", System.currentTimeMillis()));
	}

	@Override
	public void sendTerminateSignal(final TerminateReason reason, final Throwable e) {
		if (TerminateReason.UNCAUGHT_EXCEPTION == Objects.requireNonNull(reason)) {
			inlineUncaughtException = e;
		} else {
			is_terminate_method_called = true;
		}
	}

	@Override
	public void closeTask() {
		is_terminate_method_called = true;
	}

	@Override
	public void on_exception(final Throwable e) {
		sendTerminateSignal(TerminateReason.UNCAUGHT_EXCEPTION, e);
	}

	public void executeManager() {
		builder.append(utils.pack());
	}

	public void executeAssembled(final Action... actions) {
		builder.append(new AssembledAction(actions));
	}

	public void executeLinked(final Action... actions) {
		builder.append(new LinkedAction(actions));
	}

	public void executeSleep(final long sleepMS){
		builder.append(new SleepingAction(sleepMS));
	}

	public abstract Pose2d getInitialPose();
}
