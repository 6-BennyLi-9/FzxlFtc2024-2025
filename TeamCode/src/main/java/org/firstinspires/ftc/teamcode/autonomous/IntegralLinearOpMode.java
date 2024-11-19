package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.HardwareConstants;

public abstract class IntegralLinearOpMode extends LinearOpMode {
	@Override
	public final void runOpMode() throws InterruptedException {
		HardwareConstants.sync(hardwareMap, false);
		initialize();
		waitForStart();
		if(isStopRequested())return;
		linear();
	}

	public abstract void initialize();
	public abstract void linear();
}
