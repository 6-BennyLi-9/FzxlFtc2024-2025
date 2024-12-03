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

			//right suspend
			RightSample = new Pose2d(- 60, 46, toRadians(-90)),

			//right take
			RightSample1 = new Pose2d(- 36, 36, toRadians(-135)),
			RightSample2 = new Pose2d(- 47, RightSample1.getY(), RightSample1.getHeading()),
			RightSample3 = new Pose2d(- 55.5, RightSample1.getY(), RightSample1.getHeading()),

			//right take 2
			RightGetSample1 = new Pose2d(-49.5, 48, toRadians(-90)),
			RightGetSample2 = new Pose2d(- 59.5, RightGetSample1.getY(), RightGetSample1.getHeading()),

			GetSample = new Pose2d(- 50, 61, toRadians(-90)),

			LeftParkPrepare = new Pose2d(36, 12, toRadians(0));
}
