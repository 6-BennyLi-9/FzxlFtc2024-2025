package org.firstinspires.ftc.team19419.Tuning;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.team19419.Hardwares.Classic;
import org.firstinspires.ftc.team19419.Robot;
import org.firstinspires.ftc.team19419.Templates.TestProgramTemplate;
import org.firstinspires.ftc.team19419.Utils.Enums.RunningMode;

@TeleOp(name = "ClassicTest",group = "tune")
public class ClassicTest extends TestProgramTemplate {
	public Classic classic;
	public Robot lazyRobot;

	@Override
	public void opInit() {
		lazyRobot=new Robot(hardwareMap, RunningMode.TestOrTune,telemetry);
		classic=lazyRobot.classic;
		lazyRobot.registerGamepad(gamepad1,gamepad2);
	}

	@Override
	public void mainCode() {
		classic.operateThroughGamePad(lazyRobot.gamepad);
	}
}
