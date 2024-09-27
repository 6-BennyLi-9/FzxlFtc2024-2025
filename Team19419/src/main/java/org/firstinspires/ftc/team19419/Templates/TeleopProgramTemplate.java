package org.firstinspires.ftc.team19419.Templates;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.team19419.Robot;
import org.firstinspires.ftc.team19419.Utils.Enums.RunningMode;
import org.firstinspires.ftc.team19419.Utils.Timer;

public abstract class TeleopProgramTemplate extends OpMode {
	public Robot robot;
	public Timer timer;
	@Override
	public void init() {
		robot=new Robot(hardwareMap, RunningMode.ManualDrive,telemetry);
		timer=new Timer();
		whenInit();
	}

	@Override
	public void start() {
		timer.restart();
		robot.client.addData("TPS","WAIT FOR START");
	}

	@Override
	public void loop() {
		robot.client.changeData("TPS", timer.restartAndGetDeltaTime() /1000);

		whileActivating();
	}


	public abstract void whileActivating();
	public abstract void whenInit();
}
