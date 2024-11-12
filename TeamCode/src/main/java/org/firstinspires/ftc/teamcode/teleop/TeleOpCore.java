package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.GamepadRequestMemories;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.client.TelemetryClient;

@TeleOp(name = "22232",group = "Main")
public class TeleOpCore extends OpMode {
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

		robot.runThread();
	}

	@Override
	public void loop() {
		final double now=System.nanoTime()/ 1.0e9;
		TelemetryClient.getInstance()
				.changeData("time",now-start)
				.changeData("TPS",1/(now-lst));
		lst=now;
		//主程序开始


		robot.operateThroughGamepad();
//		robot.driveThroughGamepad();

		//主程序结束
		robot.printThreadDebugs();
		GamepadRequestMemories.printValues();

		robot.runThread();
	}

	@Override
	public void stop() {
		TelemetryClient.getInstance().clear();
	}
}
