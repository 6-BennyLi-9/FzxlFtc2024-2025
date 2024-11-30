package org.firstinspires.ftc.teamcode.util.ops;

import com.acmerobotics.dashboard.FtcDashboard;

import org.firstinspires.ftc.teamcode.client.Client;
import org.firstinspires.ftc.teamcode.client.DashTelemetry;
import org.firstinspires.ftc.teamcode.structure.DriveOp;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.util.RobotMng;
import org.firstinspires.ftc.teamcode.util.Timer;

public abstract class IntegralTeleOp extends OverSpeedOpMode {
	public RobotMng robot;
	public Timer timer;
	public Client client;
	private boolean auto_terminate_when_TLE = true;

	@Override
	public void op_init() {
		DriveOp.config = DriveOp.DriveConfig.StraightLinear;
		timer = new Timer();

		HardwareConstants.sync(hardwareMap, true);
		HardwareConstants.chassisConfig();
		robot = new RobotMng();
		robot.registerGamepad(gamepad1, gamepad2);
		robot.initActions();
		telemetry=new DashTelemetry(FtcDashboard.getInstance(), telemetry);
		client = new Client(telemetry);

		client.addData("TPS", "wait for start").addData("time", "wait for start").addLine("ROBOT INITIALIZE COMPLETE!").addLine("=======================");
	}

	@Override
	public void loop_init() {
		client.changeData("TPS", (1.0e3 / timer.restartAndGetDeltaTime()) + "(not started)");
		robot.runThread();//防止一些 Action 出现异常表现
	}

	@Override
	public void op_start() {
		client.deleteLine("ROBOT INITIALIZE COMPLETE!");
		timer.pushTimeTag("start");
	}

	public void auto_terminate_when_TLE(final boolean auto_terminate_when_TLE) {
		this.auto_terminate_when_TLE = auto_terminate_when_TLE;
	}

	@Override
	public void op_loop() {
		if (121 < getRuntime() && auto_terminate_when_TLE) {
			stop();
			terminateOpModeNow();
		}
		client.changeData("TPS", 1.0e3 / timer.restartAndGetDeltaTime()).changeData("time", getRuntime());
	}

	@Override
	public void op_end() {
		client.interrupt();
	}
}
