package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot;

@TeleOp(name = "22232",group = "Main")
public class Main extends OpMode {
	public Robot robot;

	@Override
	public void init() {
		robot=new Robot(hardwareMap);
		robot.registerGamepad(gamepad1,gamepad2);
		robot.initActions();
	}

	@Override
	public void loop() {
		robot.runThread();

		robot.operateThroughGamepad();
		robot.driveThroughGamepad();
	}
}
