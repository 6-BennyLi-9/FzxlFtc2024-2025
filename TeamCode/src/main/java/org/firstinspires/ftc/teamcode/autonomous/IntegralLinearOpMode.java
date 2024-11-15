package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class IntegralLinearOpMode extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		initialize();
		waitForStart();
		if(isStopRequested())return;
		linear();
	}

	public abstract void initialize();
	public abstract void linear();
}
