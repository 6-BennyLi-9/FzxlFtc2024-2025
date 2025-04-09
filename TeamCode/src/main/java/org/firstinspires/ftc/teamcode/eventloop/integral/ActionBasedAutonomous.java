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
import org.betastudio.ftc.thread.MethodFrequencyCaller;
import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.client.UpdateConfig;
import org.betastudio.ftc.ui.client.implementation.BaseMapClient;
import org.betastudio.ftc.ui.dashboard.DashTelemetry;
import org.betastudio.ftc.ui.log.FtcLogTunnel;
import org.betastudio.ftc.util.Timer;
import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.cores.UtilsMng;
import org.firstinspires.ftc.teamcode.eventloop.OverclockOpMode;
import org.firstinspires.ftc.teamcode.eventloop.TerminateReason;
import org.firstinspires.ftc.teamcode.eventloop.trajectory.HeadingTrajectoryBuilder;

import java.util.Locale;
import java.util.Objects;

public abstract class ActionBasedAutonomous extends OverclockOpMode implements IntegralOpMode, Interfaces.ThreadEx {
	public    SampleMecanumDrive       drive;
	public    UtilsMng                 utils;
	public    Timer                    timer;
	public    Client                   client;
	public    ActionBuilder            builder;
	protected boolean                  is_terminate_method_called;
	protected HeadingTrajectoryBuilder track;
	private   Exception                inlineUncaughtException;
	private   Action                   action;
	private   Runnable                 runner;

	public abstract void actionBuildEntry();

	@Override
	public void op_init() {
		FtcLogTunnel.saveAndClear();
		Global.currentOpmode = this;
		Global.registerGamepad(gamepad1, gamepad2);
		Global.prepareCoreThreadPool();
		RunMode.globalRunMode = RunMode.TELEOP;
		Global.client = client;
		timer = new Timer();
		builder = new LinkedActionBuilder();

		telemetry = new DashTelemetry(FtcDashboard.getInstance(), telemetry);
		telemetry.setAutoClear(true);
		telemetry.clearAll();
		client = new BaseMapClient(telemetry);
		client.setUpdateConfig(UpdateConfig.MANUALLY);

		final MethodFrequencyCaller caller = new MethodFrequencyCaller(client::update);
		caller.setStopRequestCaller(() -> is_terminate_method_called || isStopRequested());
		caller.setFrequencyFPS(5);
		Global.service.execute(caller);

		HardwareDatabase.sync(hardwareMap, true);
		HardwareDatabase.chassisConfig();
		utils = new UtilsMng();

		telemetry.clearAll();

		drive = new SampleMecanumDrive(hardwareMap);
		track = new HeadingTrajectoryBuilder(drive);

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
	}

	@Override
	public void op_start() {
		timer.pushTimeTag("start");

		FtcLogTunnel.MAIN.report("Op inline started successfully");
	}

	@Override
	public void op_loop() {
		client.changeData("TPS", 1.0e3 / timer.restartAndGetDeltaTime());
		client.changeData("time", getRuntime());

		if (null != inlineUncaughtException) {
			FtcLogTunnel.MAIN.report(inlineUncaughtException);
			throw new RuntimeException(inlineUncaughtException);
		}

		if (is_terminate_method_called) {
			op_end();
			terminateOpModeNow();
		}

		runner.run();
	}

	@Override
	public void op_end() {
		client.clear();

		RunMode.globalRunMode = RunMode.TERMINATE;

		if (null != inlineUncaughtException) {
			FtcLogTunnel.MAIN.report(inlineUncaughtException);
			throw new RuntimeException(inlineUncaughtException);
		}

		FtcLogTunnel.MAIN.report("Op inline closed");
		FtcLogTunnel.MAIN.save(String.format(Locale.SIMPLIFIED_CHINESE, "%tc", System.currentTimeMillis()));
	}

	@Override
	public void sendTerminateSignal(final TerminateReason reason, final Exception e) {
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
	public void exception_entry(final Throwable e) {
		sendTerminateSignal(TerminateReason.UNCAUGHT_EXCEPTION, (Exception) e);
	}

	public void inputMngAction() {
		builder.append(utils.pack());
	}

	public void appendAssembled(final Action... actions) {
		builder.append(new AssembledAction(actions));
	}

	public void appendLinked(final Action... actions) {
		builder.append(new LinkedAction(actions));
	}

	public abstract Pose2d getInitialPose();
}
