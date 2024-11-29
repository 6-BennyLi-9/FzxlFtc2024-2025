package org.firstinspires.ftc.teamcode.util.ops;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.client.Client;
import org.firstinspires.ftc.teamcode.client.DashTelemetry;
import org.firstinspires.ftc.teamcode.structure.DriveOp;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.util.RobotMng;
import org.firstinspires.ftc.teamcode.util.Timer;

public abstract class IntegralTeleOp extends OpMode {
	public RobotMng robot;
	public Timer    timer;
	public Client   client;

	@Override
	public void init() {
		DriveOp.config = DriveOp.DriveConfig.StraightLinear;
		timer = new Timer();

		HardwareConstants.sync(hardwareMap, true);
		HardwareConstants.chassisConfig();
		robot = new RobotMng();
		robot.registerGamepad(gamepad1, gamepad2);
		robot.initActions();
		client = new Client(new DashTelemetry(FtcDashboard.getInstance(), telemetry));

		client.addData("TPS", "wait for start").addData("time", "wait for start").addLine("ROBOT INITIALIZE COMPLETE!").addLine("=======================");
	}

	@Override
	public void init_loop() {
		client.changeData("TPS", (1.0e3 / timer.restartAndGetDeltaTime()) + "(not started)");
		robot.runThread();//防止一些 Action 出现异常表现
	}

	@Override
	public void start() {
		client.deleteLine("ROBOT INITIALIZE COMPLETE!");
		timer.pushTimeTag("start");
	}

	private boolean auto_terminate_when_TLE = true;

	public void auto_terminate_when_TLE(boolean auto_terminate_when_TLE) {
		this.auto_terminate_when_TLE = auto_terminate_when_TLE;
	}

	@Override
	public void loop() {
		if (121 < getRuntime() && auto_terminate_when_TLE) {
			stop();
			terminateOpModeNow();
		}
		client.changeData("TPS", 1.0e3 / timer.restartAndGetDeltaTime()).changeData("time", getRuntime());
	}

	@Override
	public void stop() {
		client.interrupt();
	}
}
