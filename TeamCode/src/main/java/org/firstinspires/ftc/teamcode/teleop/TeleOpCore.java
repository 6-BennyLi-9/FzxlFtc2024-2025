package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.GamepadRequestMemories;
import org.firstinspires.ftc.teamcode.HardwareConstants;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.client.TelemetryClient;
import org.firstinspires.ftc.teamcode.structure.DriveOption;

@TeleOp(name = "22232",group = "Main")
public class TeleOpCore extends OpMode {
	public Robot robot;

	@Override
	public void init() {
		DriveOption.setDriveUsingPID(false);

		robot=new Robot(hardwareMap);
		robot.registerGamepad(gamepad1,gamepad2);
		robot.initActions();
		HardwareConstants.chassisConfig();
		TelemetryClient.registerInstance(new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry()));

		TelemetryClient.getInstance()
				.addData("TPS","wait for start")
				.addData("time","wait for start");
	}

	/**单位：秒*/
	public double start,lst;

	@Override
	public void init_loop() {
		start=System.nanoTime()/ 1.0e9;
		lst=start;

		robot.runThread();

		TelemetryClient.getInstance()
				.addLine("ROBOT INITIALIZE COMPLETE!")
				.addLine("=======================");
	}

	@Override
	public void start() {
		TelemetryClient.getInstance().deleteLine("ROBOT INITIALIZE COMPLETE!");
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
		robot.driveThroughGamepad();

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
