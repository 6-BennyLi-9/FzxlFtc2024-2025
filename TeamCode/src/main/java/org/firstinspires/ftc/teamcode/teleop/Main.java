package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.GamepadRequestMemories;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.client.TelemetryClient;

@TeleOp(name = "22232",group = "Main")
public class Main extends OpMode {
	public Robot robot;

	@Override
	public void init() {
		robot=new Robot(hardwareMap);
		robot.registerGamepad(gamepad1,gamepad2);
		robot.initActions();
		TelemetryClient.registerInstance(new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry()));

		TelemetryClient.getInstance()
				.addData("TPS","wait for start")
				.addData("time","wait for start")
				.addLine("=================");
	}

	/**单位：秒*/
	public double start,lst;

	@Override
	public void init_loop() {
		start=System.nanoTime()/ 1.0e9;
		lst=start;
	}

	@Override
	public void loop() {
		final double now=System.nanoTime()/ 1.0e9;
		TelemetryClient.getInstance()
				.changeData("time",now-start)
				.changeData("TPS",1/(now-lst));
		lst=now;

		robot.runThread();

		robot.operateThroughGamepad();
		robot.driveThroughGamepad();

		robot.printThreadDebugs();
		GamepadRequestMemories.printValues();
	}

	@Override
	public void stop() {
		TelemetryClient.getInstance().clear();
	}
}
