package org.firstinspires.ftc.team19419.Samples;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.team19419.Templates.TeleopProgramTemplate;

@TeleOp(name = "ManualCodeSample",group = "samples")
public class ManualCodeSample extends TeleopProgramTemplate {
	@Override
	public void whenInit() {
		robot.registerGamepad(gamepad1,gamepad2);
	}

	@Override
	public void whileActivating() {
		robot.operateThroughGamePad();
		robot.update();
	}

}
