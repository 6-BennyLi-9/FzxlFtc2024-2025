package org.firstinspires.ftc.cores.eventloop;

import com.acmerobotics.dashboard.FtcDashboard;

import org.betastudio.ftc.client.BranchThreadClient;
import org.betastudio.ftc.client.Client;
import org.betastudio.ftc.client.DashTelemetry;
import org.firstinspires.ftc.cores.RobotMng;
import org.firstinspires.ftc.cores.structure.DriveMode;
import org.firstinspires.ftc.cores.structure.DriveOp;
import org.firstinspires.ftc.teamcode.CoreDatabase;
import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.RunMode;
import org.firstinspires.ftc.teamcode.Timer;

import java.util.Objects;

public abstract class IntegralTeleOp extends OverclockOpMode implements IntegralOpMode {
	public  RobotMng  robot;
	public  Timer     timer;
	public  Client    client;
	private boolean   auto_terminate_when_TLE = true;
	private Exception inlineUncaughtException = null;

	@Override
	public void op_init() {
		Global.currentOpmode = this;
		Global.registerGamepad(gamepad1, gamepad2);
		Global.prepareCoreThreadPool();
		Global.runMode = RunMode.TELEOP;
		DriveOp.config = DriveMode.STRAIGHT_LINEAR;
		timer = new Timer();

		telemetry = new DashTelemetry(FtcDashboard.getInstance(), telemetry);
		telemetry.setAutoClear(true);
		client = new BranchThreadClient(telemetry);

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

		if (CoreDatabase.autonomous_time_used != -1){
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

		if (inlineUncaughtException != null) {
			throw new RuntimeException(inlineUncaughtException);
		}
	}

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
	public void sendTerminateSignal(TerminateReason reason) {
		sendTerminateSignal(reason, new NullPointerException("UnModified"));
	}

	@Override
	public void sendTerminateSignal(TerminateReason reason, Exception e) {
		if (Objects.requireNonNull(reason) == TerminateReason.UNCAUGHT_EXCEPTION) {
			inlineUncaughtException = e;
		} else {
			terminateOpModeNow();
		}
	}
}
