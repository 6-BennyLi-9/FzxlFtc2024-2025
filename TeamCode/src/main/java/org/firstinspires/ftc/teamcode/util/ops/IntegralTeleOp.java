package org.firstinspires.ftc.teamcode.util.ops;

import com.acmerobotics.dashboard.FtcDashboard;

import org.betastudio.ftc.client.DashTelemetry;
import org.betastudio.ftc.client.TelemetryClient;
import org.firstinspires.ftc.teamcode.structure.DriveConfig;
import org.firstinspires.ftc.teamcode.structure.DriveOp;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.util.Timer;
import org.firstinspires.ftc.teamcode.util.mng.RobotMng;

public abstract class IntegralTeleOp extends OverSpeedOpMode {
	public  RobotMng robot;
	public  Timer           timer;
	public  TelemetryClient client;
	private boolean         auto_terminate_when_TLE = true;

	@Override
	public void op_init() {
		DriveOp.config = DriveConfig.StraightLinear;
		timer = new Timer();

		HardwareConstants.sync(hardwareMap, true);
		HardwareConstants.chassisConfig();
		robot = new RobotMng();
		robot.registerGamepad(gamepad1, gamepad2);
		robot.initActions();
		telemetry = new DashTelemetry(FtcDashboard.getInstance(), telemetry);
		client = new TelemetryClient(telemetry);

		telemetry.clearAll();
		client.autoUpdate=false;

		client.addData("TPS", "wait for start").addData("time", "wait for start").addLine("ROBOT INITIALIZE COMPLETE!").addLine("=======================");
	}

	@Override
	public void loop_init() {
		client.changeData("TPS", (1.0e3 / timer.restartAndGetDeltaTime()) + "(not started)");
		robot.runThread();//防止一些 Action 出现异常表现

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
	}
}
