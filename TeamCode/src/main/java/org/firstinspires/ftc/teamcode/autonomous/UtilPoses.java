package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;

@SuppressWarnings("unused")
public enum UtilPoses {
	;
	public static Pose2d
	LeftStart =new Pose2d(12,60,Math.toRadians(90)),
	RightStart =new Pose2d(-12,60,Math.toRadians(90)),

	Decant =new Pose2d(60,50,Math.toRadians(-135)),

	LeftSuspend =new Pose2d(LeftStart.getX(),32,Math.toRadians(90)),
	RightSuspend =new Pose2d(RightStart.getX(),32,Math.toRadians(90)),

	LeftSample1 =new Pose2d(49,24,Math.toRadians(0)),
	LeftSample2 =new Pose2d(50, LeftSample1.getY(),Math.toRadians(0)),
	LeftSample3 =new Pose2d(52, LeftSample1.getY(),Math.toRadians(0)),

	RightSample1 =new Pose2d(-48,23,Math.toRadians(180)),
	RightSample2 =new Pose2d(-50, RightSample1.getY(),Math.toRadians(180)),
	RightSample3 =new Pose2d(-52, RightSample1.getY(),Math.toRadians(180)),

	GetSample =new Pose2d(-48,60,Math.toRadians(90)),


	LeftPark =new Pose2d(25,12,Math.toRadians(0)),
	RightPark =new Pose2d(-25,12,Math.toRadians(180));
}
