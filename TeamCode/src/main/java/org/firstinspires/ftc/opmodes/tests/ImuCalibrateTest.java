package org.firstinspires.ftc.opmodes.tests;

import static org.betastudio.ftc.Annotations.TestShelved;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.eventloop.integral.ActionBasedAutonomous;

@Disabled
@TestShelved
@Autonomous(group = "9_Tests")
public class ImuCalibrateTest extends ActionBasedAutonomous {
	@Override
	public void actionBuildEntry() {
		utils.imuCalibrate(0, drive, new Pose2d());
		utils.imuCalibrate(90, drive, new Pose2d());
		utils.imuCalibrate(0, drive, new Pose2d());
		executeManager();
	}

	@Override
	public Pose2d getInitialPose() {
		return new Pose2d();
	}
}
