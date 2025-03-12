package org.firstinspires.ftc.teamcode.cores.eventloop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;

import org.acmerobotics.roadrunner.SampleMecanumDrive;
import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.RunMode;
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
import org.firstinspires.ftc.teamcode.cores.eventloop.commands.ActionCommand;
import org.firstinspires.ftc.teamcode.cores.eventloop.commands.Command;
import org.firstinspires.ftc.teamcode.cores.eventloop.commands.TrajectoryCommand;
import org.firstinspires.ftc.teamcode.cores.eventloop.commands.TrajectorySequenceCommand;
import org.firstinspires.ftc.teamcode.cores.structure.DriveMode;
import org.firstinspires.ftc.teamcode.cores.structure.DriveOp;

import java.util.Locale;
import java.util.Queue;

public abstract class LoopCommandAutonomous extends OverclockOpMode implements IntegralOpMode, Interfaces.ThreadEx {
	public    SampleMecanumDrive drive;
	public    Client             client;
	public    UtilsMng           utils;
	public    Timer              timer;
	protected boolean            is_terminate_method_called;
	protected Exception          inline_exception;
	protected Queue <Command>    commands;
	protected TerminateReason    reason;

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
	public void op_loop() {
		drive.update();
		if (! drive.isBusy() && ! commands.isEmpty()) {
			Command element = commands.remove();
			if (element instanceof TrajectoryCommand) {
				((TrajectoryCommand) element).execute(drive);
			} else if (element instanceof TrajectorySequenceCommand) {
				((TrajectorySequenceCommand) element).execute(drive);
			} else if (element instanceof ActionCommand) {
				((ActionCommand) element).execute();
			}
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

		client.update();
	}

	@Override
	public void sendTerminateSignal(TerminateReason reason, Exception e) {
		is_terminate_method_called = true;
		inline_exception = e;
		this.reason = reason;
	}

	@Override
	public void closeTask() {
		is_terminate_method_called = true;
	}

	public abstract void commandOverload();

	@Override
	public void exception_entry(final Throwable e) {
		sendTerminateSignal(TerminateReason.UNCAUGHT_EXCEPTION, (Exception) e);
	}
}
