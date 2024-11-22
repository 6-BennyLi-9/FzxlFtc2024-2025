package org.firstinspires.ftc.teamcode.autonomous.utils;

import com.acmerobotics.roadrunner.geometry.Pose2d;

@SuppressWarnings("unused")
public enum UtilPoses {
	;
	public static Pose2d
	BlueLeftStart=new Pose2d(12,62,Math.toRadians(-90)),
	BlueRightStart=new Pose2d(-12,62,Math.toRadians(-90)),
	RedLeftStart=new Pose2d(-12,-62,Math.toRadians(90)),
	RedRightStart=new Pose2d(12,-62,Math.toRadians(90)),

	BlueDecant=new Pose2d(55,55,Math.toRadians(-135)),
	RedDecant=new Pose2d(-55,-55,Math.toRadians(135)),

	BlueLeftSuspend=new Pose2d(BlueLeftStart.getX(),32,Math.toRadians(90)),
	BlueRightSuspend=new Pose2d(BlueRightStart.getX(),32,Math.toRadians(90)),
	RedLeftSuspend=new Pose2d(RedLeftStart.getX(),-32,Math.toRadians(90)),
	RedRightSuspend=new Pose2d(RedRightStart.getX(),-32,Math.toRadians(90)),

	BlueLeftSample1=new Pose2d(48,23,Math.toRadians(0)),
	BlueLeftSample2=new Pose2d(50,BlueLeftSample1.getY(),Math.toRadians(0)),
	BlueLeftSample3=new Pose2d(52,BlueLeftSample1.getY(),Math.toRadians(0)),

	RedLeftSample1=new Pose2d(48,-23,Math.toRadians(0)),
	RedLeftSample2=new Pose2d(50,RedLeftSample1.getY(),Math.toRadians(0)),
	RedLeftSample3=new Pose2d(52,RedLeftSample1.getY(),Math.toRadians(0)),

	BlueRightSample1=new Pose2d(-48,23,Math.toRadians(180)),
	BlueRightSample2=new Pose2d(-50,BlueRightSample1.getY(),Math.toRadians(180)),
	BlueRightSample3=new Pose2d(-52,BlueRightSample1.getY(),Math.toRadians(180)),

	BlueGetSample=new Pose2d(-48,60,Math.toRadians(90)),
	RedGetSample=new Pose2d(-48,-60,Math.toRadians(90)),

	RedRightSample1=new Pose2d(-48,-23,Math.toRadians(180)),
	RedRightSample2=new Pose2d(-50,RedRightSample1.getY(),Math.toRadians(180)),
	RedRightSample3=new Pose2d(-52,RedRightSample1.getY(),Math.toRadians(180)),

	BlueLeftPark=new Pose2d(30,12,Math.toRadians(180)),
	BlueRightPark=new Pose2d(-30,12,Math.toRadians(0)),
	RedLeftPark=new Pose2d(-30,-12,Math.toRadians(0)),
	RedRightPark=new Pose2d(30,-12,Math.toRadians(180));
}
