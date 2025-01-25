package org.firstinspires.ftc.cores.eventloop;

import com.acmerobotics.dashboard.FtcDashboard;

import org.betastudio.ftc.client.BranchThreadClient;
import org.betastudio.ftc.client.Client;
import org.betastudio.ftc.dashboard.DashTelemetry;
import org.betastudio.ftc.util.ThreadEx;
import org.firstinspires.ftc.cores.RobotMng;
import org.firstinspires.ftc.cores.structure.DriveMode;
import org.firstinspires.ftc.cores.structure.DriveOp;
import org.firstinspires.ftc.teamcode.CoreDatabase;
import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.RunMode;
import org.betastudio.ftc.util.Timer;

import java.util.Objects;

public abstract class IntegralTeleOp extends OverclockOpMode implements IntegralOpMode , ThreadEx {
	public    RobotMng  robot;
	public    Timer     timer;
	public    Client    client;
	protected boolean   is_terminate_method_called;
	private   boolean   auto_terminate_when_TLE;
	private   Exception inlineUncaughtException;

	@Override
	public void op_init() {
		Global.currentOpmode = this;
		Global.registerGamepad(gamepad1, gamepad2);
		Global.prepareCoreThreadPool();
		Global.runMode = RunMode.TELEOP;
		Global.client=client;
		DriveOp.config = DriveMode.STRAIGHT_LINEAR;
		timer = new Timer();

		telemetry = new DashTelemetry(FtcDashboard.getInstance(), telemetry);
		telemetry.setAutoClear(true);
		client = new BranchThreadClient(telemetry,10);

		HardwareDatabase.sync(hardwareMap, true);
		HardwareDatabase.chassisConfig();
		robot = new RobotMng();
		robot.fetchClient(client);
		robot.initControllers();

		telemetry.clearAll();

		client	.addData("TPS", "wait for start")
				.addData("time", "wait for start")
				.addLine("ROBOT INITIALIZE COMPLETE!")
				.addLine("=======================");

		if (- 1 != CoreDatabase.autonomous_time_used){
			client	.addData("last autonomous time used",CoreDatabase.autonomous_time_used)
					.addData("last terminateReason",CoreDatabase.last_terminateReason.name());
		}
	}

	@Override
	public void loop_init() {
		client.changeData("TPS", (1.0e3 / timer.restartAndGetDeltaTime()) + "(not started)");
		robot.update();//防止一些 Action 出现异常表现
	}

	@Override
	public void op_start() {
		client	.deleteLine("ROBOT INITIALIZE COMPLETE!")
				.deleteData("last autonomous time used")
				.deleteData("last terminateReason");
		timer.pushTimeTag("start");
	}

	public void auto_terminate_when_TLE(final boolean auto_terminate_when_TLE) {
		this.auto_terminate_when_TLE = auto_terminate_when_TLE;
	}

	public boolean auto_terminate_when_TLE() {
		return auto_terminate_when_TLE;
	}

	@Override
	public void op_loop() {
		if (121 < getRuntime() && auto_terminate_when_TLE) {
			stop();
			terminateOpModeNow();
		}
		client.changeData("TPS", 1.0e3 / timer.restartAndGetDeltaTime()).changeData("time", getRuntime());

		if (null != inlineUncaughtException) {
			throw new RuntimeException(inlineUncaughtException);
		}

		if (is_terminate_method_called){
			op_end();
			terminateOpModeNow();
		}

		try {
			op_loop_entry();
		}catch (final UnsupportedOperationException ignored){}
	}

	public abstract void op_loop_entry();

	@Override
	public void op_end() {
		client.clear();
		if (client instanceof BranchThreadClient){
			((BranchThreadClient) client).closeTask();
		}

		Global.runMode = RunMode.TERMINATE;

		CoreDatabase.writeInVals(this, TerminateReason.USER_ACTIONS);
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
			is_terminate_method_called=true;
		}
	}

	@Override
	public void closeTask() {
		is_terminate_method_called=true;
	}
}
