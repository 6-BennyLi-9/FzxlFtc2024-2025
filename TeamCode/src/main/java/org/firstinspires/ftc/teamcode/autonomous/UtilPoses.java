package org.firstinspires.ftc.teamcode.autonomous;

import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.geometry.Pose2d;

public enum UtilPoses {
	;
	public static Pose2d
			LeftStart = new Pose2d(12, 60, toRadians(90)),
			RightStart = new Pose2d(- 12, 60, toRadians(90)),

			Decant = new Pose2d(60, 54, toRadians(- 135)),

			LeftSuspend = new Pose2d(LeftStart.getX(), 32, toRadians(90)),
			RightSuspend = new Pose2d(RightStart.getX(), 32, toRadians(90)),

			LeftSample = new Pose2d(60,47, toRadians(-90)),
			RightSample = new Pose2d(- 60, 47, toRadians(-90)),

			GetSample = new Pose2d(- 48, 60, toRadians(90)),

			LeftParkPrepare = new Pose2d(36, 12, toRadians(0));
}
