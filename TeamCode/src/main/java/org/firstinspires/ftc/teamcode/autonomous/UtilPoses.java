package org.firstinspires.ftc.teamcode.autonomous;

import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.geometry.Pose2d;

public final class UtilPoses {
	public static Pose2d
	//正规：y-0.5
	LeftStart = new Pose2d(12, 60, toRadians(90)),
	RightStart = new Pose2d(- 12, 60, toRadians(90)),

	Decant = new Pose2d(59, 52, toRadians(- 135)),

	LeftSuspend = new Pose2d(LeftStart.getX(), 32, toRadians(90)),
	RightSuspend = new Pose2d(RightStart.getX(), 32, toRadians(90)),

	LeftSample = new Pose2d(59, 48, toRadians(- 90)),

	//right suspend
	RightSample = new Pose2d(- 60, 46, toRadians(- 90)),

	//right take
	RightFirstSample = new Pose2d(- 36, 36, toRadians(- 135)),
	RightSecondSample = new Pose2d(- 47, RightFirstSample.getY(), RightFirstSample.getHeading()),
	RightThirdSample = new Pose2d(- 55.5, RightFirstSample.getY(), RightFirstSample.getHeading()),

	//right take 2
	RightGetFirstSample = new Pose2d(- 49.5, 45, toRadians(- 90)),
	RightGetSecondSample = new Pose2d(- 61, RightGetFirstSample.getY(), RightGetFirstSample.getHeading()),

	GetSample = new Pose2d(- 50, 61, toRadians(- 90)),

	LeftParkPrepare = new Pose2d(36, 12, toRadians(0)),
	RightParkPrepare = new Pose2d(-40, 12, toRadians(180));
}
