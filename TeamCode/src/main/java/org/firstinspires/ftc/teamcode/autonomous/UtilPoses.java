package org.firstinspires.ftc.teamcode.autonomous;

import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.geometry.Pose2d;

@SuppressWarnings("unused")
public enum UtilPoses {
	;
	public static Pose2d
			LeftStart = new Pose2d(12, 60, toRadians(90)),
			RightStart = new Pose2d(- 12, 60, toRadians(90)),

			Decant = new Pose2d(60, 50, toRadians(- 135)),

			LeftSuspend = new Pose2d(LeftStart.getX(), 32, toRadians(90)),
			RightSuspend = new Pose2d(RightStart.getX(), 32, toRadians(90)),

			LeftSample1 = new Pose2d(49, 24, toRadians(0)),
			LeftSample2 = new Pose2d(60, LeftSample1.getY(), toRadians(0)),
			LeftSample3 = new Pose2d(66, LeftSample1.getY(), toRadians(0)),

			RightSample1 = new Pose2d(- 48, 23, toRadians(180)),
			RightSample2 = new Pose2d(- 60, RightSample1.getY(), toRadians(180)),
			RightSample3 = new Pose2d(- 66, RightSample1.getY(), toRadians(180)),

			GetSample = new Pose2d(- 48, 60, toRadians(90)),

			LeftPark = new Pose2d(25, 12, toRadians(0));
}
