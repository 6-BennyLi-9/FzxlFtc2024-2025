package org.firstinspires.ftc.teamcode.cores.eventloop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;

import org.acmerobotics.roadrunner.SampleMecanumDrive;
import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.RunMode;
import org.betastudio.ftc.action.packages.ActionPackage;
import org.betastudio.ftc.action.packages.TaggedActionPackage;
import org.betastudio.ftc.time.Timer;
import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.client.UpdateConfig;
import org.betastudio.ftc.ui.client.implementation.BaseMapClient;
import org.betastudio.ftc.ui.dashboard.DashTelemetry;
import org.betastudio.ftc.ui.log.FtcLogTunnel;
import org.firstinspires.ftc.teamcode.CoreDatabase;
import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.cores.UtilsMng;
import org.firstinspires.ftc.teamcode.cores.structure.DriveMode;
import org.firstinspires.ftc.teamcode.cores.structure.DriveOp;

import java.util.Locale;
import java.util.Objects;

public abstract class LoopCommandAutonomous extends OverclockOpMode implements IntegralOpMode, Interfaces.ThreadEx {
	public    SampleMecanumDrive drive;
	public    Client             client;
	public    UtilsMng           utils;
	public    Timer              timer;
	protected Exception          inline_exception;
	protected ActionPackage      commands;
	protected TerminateReason    reason;
	private   boolean            is_terminate_method_called;
	private   boolean            isCommandUndone;

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

		commands = new TaggedActionPackage();
		commandOverload();

		HardwareDatabase.sync(hardwareMap, false);
		HardwareDatabase.chassisConfig();

		telemetry.clearAll();

		client.putData("TPS", "wait for start");
		client.putData("time", "wait for start");
		client.putLine("ROBOT INITIALIZE COMPLETE!");
		client.putLine("=======================");
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

	@Override
	public void op_loop() {
		drive.update();
		if (! drive.isBusy() && ! isCommandUndone) {
			isCommandUndone = commands.activate();
		}

		if (is_terminate_method_called){
			CoreDatabase.writeInVals(this, reason, timer.getDeltaTime());
			if (inline_exception != null) {
				if (inline_exception instanceof OpModeManagerImpl.ForceStopException) {
					closeTask();
				} else {
					FtcLogTunnel.MAIN.report(inline_exception);
					FtcLogTunnel.MAIN.save(String.format(Locale.SIMPLIFIED_CHINESE, "%tc", System.currentTimeMillis()));
					throw new RuntimeException(inline_exception);
				}
			}else{
				FtcLogTunnel.MAIN.save(String.format(Locale.SIMPLIFIED_CHINESE, "%tc", System.currentTimeMillis()));
			}
		}

		client.changeData("TPS", 1.0e3 / timer.restartAndGetDeltaTime());
		client.changeData("time", getRuntime());
		client.update();
	}

	public abstract void commandOverload();

	@Override
	public void op_end() {
		client.clear();

		Global.runMode = RunMode.TERMINATE;

		if (null != inline_exception) {
			FtcLogTunnel.MAIN.report(inline_exception);
			throw new RuntimeException(inline_exception);
		}

		FtcLogTunnel.MAIN.report("Op inline closed");
		FtcLogTunnel.MAIN.save(String.format(Locale.SIMPLIFIED_CHINESE, "%tc", System.currentTimeMillis()));
	}

	@Override
	public void sendTerminateSignal(final TerminateReason reason, final Exception e) {
		if (TerminateReason.UNCAUGHT_EXCEPTION == Objects.requireNonNull(reason)) {
			inline_exception = e;
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
