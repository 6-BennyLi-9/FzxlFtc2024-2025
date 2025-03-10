package org.acmerobotics.roadrunner.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.acmerobotics.roadrunner.SampleMecanumDrive;

/*
 * This is a simple routine to test turning capabilities.
 */
@Disabled
@Config
@Autonomous(group = "drive")
public class TurnTest extends LinearOpMode {
	public static final double ANGLE = 180; // deg

	@Override
	public void runOpMode() throws InterruptedException {
		final SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

		waitForStart();

		if (isStopRequested()) return;

		drive.turn(Math.toRadians(ANGLE));
	}
}
