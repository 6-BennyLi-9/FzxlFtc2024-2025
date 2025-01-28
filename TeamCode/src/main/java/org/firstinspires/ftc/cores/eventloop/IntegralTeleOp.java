package org.firstinspires.ftc.cores.eventloop;

import com.acmerobotics.dashboard.FtcDashboard;

import org.betastudio.ftc.entry.ThreadEx;
import org.betastudio.ftc.ui.client.BranchThreadClient;
import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.dashboard.DashTelemetry;
import org.betastudio.ftc.ui.log.FtcLogTunnel;
import org.betastudio.ftc.util.time.Timer;
import org.firstinspires.ftc.cores.RobotMng;
import org.firstinspires.ftc.cores.structure.DriveMode;
import org.firstinspires.ftc.cores.structure.DriveOp;
import org.firstinspires.ftc.teamcode.CoreDatabase;
import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.RunMode;

import java.util.Locale;
import java.util.Objects;

public abstract class IntegralTeleOp extends OverclockOpMode implements IntegralOpMode, ThreadEx {
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
		client = new BranchThreadClient(telemetry, 10);

		HardwareDatabase.sync(hardwareMap, true);
		HardwareDatabase.chassisConfig();
		robot = new RobotMng();
		robot.fetchClient(client);

		telemetry.clearAll();

		client.addData("TPS", "wait for start").addData("time", "wait for start").addLine("ROBOT INITIALIZE COMPLETE!").addLine("=======================");

		if (- 1 != CoreDatabase.autonomous_time_used) {
			client.addData("last autonomous time used", CoreDatabase.autonomous_time_used).addData("last terminateReason", CoreDatabase.last_terminateReason.name());
		}

		FtcLogTunnel.MAIN.report("Op inline initialized");
	}

	@Override
	public void loop_init() {
		client.changeData("TPS", (1.0e3 / timer.restartAndGetDeltaTime()) + "(not started)");
	}

	@Override
	public void op_start() {
		client.deleteLine("ROBOT INITIALIZE COMPLETE!").deleteData("last autonomous time used").deleteData("last terminateReason");
		timer.pushTimeTag("start");

		FtcLogTunnel.MAIN.report("Op inline started successfully");
	}

	public void auto_terminate_when_TLE(final boolean auto_terminate_when_TLE) {
		this.auto_terminate_when_TLE = auto_terminate_when_TLE;
	}

	public boolean auto_terminate_when_TLE() {
		return auto_terminate_when_TLE;
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
		client.changeData("TPS", 1.0e3 / timer.restartAndGetDeltaTime()).changeData("time", getRuntime());

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
		} catch (final UnsupportedOperationException exception) {
			FtcLogTunnel.MAIN.report(exception);
		}
	}

	public abstract void op_loop_entry();

	@Override
	public void op_end() {
		client.clear();
		if (client instanceof BranchThreadClient) {
			((BranchThreadClient) client).closeTask();
		}

		Global.runMode = RunMode.TERMINATE;

		CoreDatabase.writeInVals(this, TerminateReason.USER_ACTIONS);

		FtcLogTunnel.MAIN.report("Op inline closed");
		FtcLogTunnel.MAIN.save(String.format(Locale.SIMPLIFIED_CHINESE, "%tc", System.currentTimeMillis()));

		if (null != inlineUncaughtException) {
			FtcLogTunnel.MAIN.report(inlineUncaughtException);
			throw new RuntimeException(inlineUncaughtException);
		}
	}

	@Override
	public void sendTerminateSignal(final TerminateReason reason) {
		sendTerminateSignal(reason, new OpTerminateException(reason.name()));
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
