package org.firstinspires.ftc.teamcode.cores.eventloop;

import com.acmerobotics.dashboard.FtcDashboard;

import org.betastudio.ftc.time.Timer;
import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.client.UpdateConfig;
import org.betastudio.ftc.ui.client.implementation.BaseMapClient;
import org.betastudio.ftc.ui.dashboard.DashTelemetry;
import org.betastudio.ftc.ui.log.FtcLogTunnel;
import org.betastudio.ftc.Interfaces;
import org.firstinspires.ftc.teamcode.Local;
import org.firstinspires.ftc.teamcode.cores.RobotMng;
import org.firstinspires.ftc.teamcode.cores.structure.DriveMode;
import org.firstinspires.ftc.teamcode.cores.structure.DriveOp;
import org.firstinspires.ftc.teamcode.CoreDatabase;
import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.betastudio.ftc.RunMode;

import java.util.Locale;
import java.util.Objects;

public abstract class IntegralTeleOp extends OverclockOpMode implements IntegralOpMode, Interfaces.ThreadEx {
	public    RobotMng  robot;
	public    Timer     timer;
	public    Client    client;
	protected boolean   is_terminate_method_called;
	protected boolean   initialized;
	private   boolean   auto_terminate_when_TLE;
	private   Exception inlineUncaughtException;

	@Override
	public void op_init() {
		FtcLogTunnel.saveAndClear();
		Global.currentOpmode = this;
		Global.registerGamepad(gamepad1, gamepad2);
		Global.prepareCoreThreadPool();
		Global.runMode = RunMode.TELEOP;
		Global.client = client;
		DriveOp.config = DriveMode.STRAIGHT_LINEAR;
		timer = new Timer();

		telemetry = new DashTelemetry(FtcDashboard.getInstance(), telemetry);
		telemetry.setAutoClear(true);
		client = new BaseMapClient(telemetry);
		client.setUpdateConfig(UpdateConfig.MANUALLY);

		Global.service.execute(()->{
			FtcLogTunnel.MAIN.report("start client updater successful");
			while (!isStopRequested()){
				client.update();
				Local.sleep(100);
			}
		});

		HardwareDatabase.sync(hardwareMap, true);
		HardwareDatabase.chassisConfig();
		robot = new RobotMng();
		robot.fetchClient(client);

		telemetry.clearAll();

		client.putData("TPS", "wait for start");
		client.putData("time", "wait for start");
		client.putLine("ROBOT INITIALIZE COMPLETE!");
		client.putLine("=======================");

		if (- 1 != CoreDatabase.autonomous_time_used) {
			client.putData("last autonomous time used", CoreDatabase.autonomous_time_used);
			client.putData("last terminateReason", CoreDatabase.last_terminate_reason.name());
		}

		FtcLogTunnel.MAIN.report("Op inline initialized");
	}

	@Override
	public void loop_init() {
		client.changeData("TPS", (1.0e3 / timer.restartAndGetDeltaTime()) + "(not started)");
	}

	@Override
	public void op_start() {
		client.deleteLine("ROBOT INITIALIZE COMPLETE!");
		client.deleteData("last autonomous time used");
		client.deleteData("last terminateReason");
		timer.pushTimeTag("start");

		FtcLogTunnel.MAIN.report("Op inline started successfully");
	}

	public void auto_terminate_when_TLE(final boolean auto_terminate_when_TLE) {
		this.auto_terminate_when_TLE = auto_terminate_when_TLE;
	}

	@Override
	public void op_loop() {
		if (! initialized) {
			initialized = true;
			robot.initControllers();
		}
		if (121 < getRuntime() && auto_terminate_when_TLE) {
			stop();
			terminateOpModeNow();
		}
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

		try {
			op_loop_entry();
		} catch (final Exception exception) {
			exception_entry(exception);
		}
	}

	public abstract void op_loop_entry();

	@Override
	public void op_end() {
		client.clear();

		Global.runMode = RunMode.TERMINATE;

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
}
