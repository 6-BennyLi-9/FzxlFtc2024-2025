package org.firstinspires.ftc.opmodes.autonomous;

import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.geometry.Pose2d;

public final class UtilPoses {
	public static final Pose2d LeftStart  = new Pose2d(12, 60, toRadians(90));
	public static final Pose2d RightStart = new Pose2d(- 12, 60, toRadians(90));

	public static final Pose2d Decant = new Pose2d(59, 55, toRadians(- 135));

	public static final Pose2d LeftSuspend  = new Pose2d(LeftStart.getX(), 32, toRadians(90));
	public static final Pose2d RightSuspend = new Pose2d(RightStart.getX(), 32, toRadians(90));

	public static final Pose2d LeftSample = new Pose2d(59, 50, toRadians(- 90));

	public static Pose2d//right suspend
	RightSample = new Pose2d(- 60, 46, toRadians(- 90));

	public static final Pose2d//right take
						 RightFirstSample  = new Pose2d(- 36, 36, toRadians(- 135));
	public static       Pose2d RightSecondSample = new Pose2d(- 47, RightFirstSample.getY(), RightFirstSample.getHeading());
	public static Pose2d RightThirdSample = new Pose2d(- 55.5, RightFirstSample.getY(), RightFirstSample.getHeading());

	public static final Pose2d//right take 2
							   RightGetFirstSample  = new Pose2d(- 49.5, 47.5, toRadians(- 90));
	public static final Pose2d RightGetSecondSample = new Pose2d(- 60, RightGetFirstSample.getY(), RightGetFirstSample.getHeading());

	public static final Pose2d GetSample = new Pose2d(- 50, 61, toRadians(- 90));

	public static final Pose2d LeftParkPrepare  = new Pose2d(36, 10, toRadians(0));
	public static       Pose2d RightParkPrepare = new Pose2d(- 40, 12, toRadians(180));
}
