package org.firstinspires.ftc.teamcore.eventloop;

import com.acmerobotics.dashboard.FtcDashboard;

import org.betastudio.ftc.client.Client;
import org.betastudio.ftc.client.DashTelemetry;
import org.betastudio.ftc.client.TelemetryClient;
import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.RunMode;
import org.firstinspires.ftc.teamcore.structure.DriveConfig;
import org.firstinspires.ftc.teamcore.structure.DriveOp;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.Timer;
import org.firstinspires.ftc.teamcore.RobotMng;

public abstract class IntegralTeleOp extends OverclockOpMode {
	public  RobotMng        robot;
	public  Timer   timer;
	public  Client  client;
	private boolean auto_terminate_when_TLE = true;

	@Override
	public void op_init() {
		Global.registerGamepad(gamepad1,gamepad2);
		Global.prepareCoreThreadPool();
		Global.currentMode= RunMode.TeleOping;
		DriveOp.config = DriveConfig.StraightLinear;
		timer = new Timer();

		HardwareDatabase.sync(hardwareMap, true);
		HardwareDatabase.chassisConfig();
		robot = new RobotMng();
		robot.initControllers();
		telemetry = new DashTelemetry(FtcDashboard.getInstance(), telemetry);
		client = new TelemetryClient(telemetry);

		telemetry.clearAll();
		client.setAutoUpdate(false);

		client.addData("TPS", "wait for start").addData("time", "wait for start").addLine("ROBOT INITIALIZE COMPLETE!").addLine("=======================");
	}

	@Override
	public void loop_init() {
		client.changeData("TPS", (1.0e3 / timer.restartAndGetDeltaTime()) + "(not started)");
		robot.update();//防止一些 Action 出现异常表现

		client.update();
	}

	@Override
	public void op_start() {
		client.deleteLine("ROBOT INITIALIZE COMPLETE!");
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
		client.update();

		if (121 < getRuntime() && auto_terminate_when_TLE) {
			stop();
			terminateOpModeNow();
		}
		client.changeData("TPS", 1.0e3 / timer.restartAndGetDeltaTime()).changeData("time", getRuntime());
	}

	@Override
	public void op_end() {
		client.clear();
		Global.currentMode=RunMode.Terminated;
	}
}
