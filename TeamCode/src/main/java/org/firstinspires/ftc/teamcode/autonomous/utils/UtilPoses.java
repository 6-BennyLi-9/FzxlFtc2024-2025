package org.firstinspires.ftc.teamcode.autonomous.utils;

import com.acmerobotics.roadrunner.geometry.Pose2d;

@SuppressWarnings("unused")
public enum UtilPoses {
	;
	//起始点位
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

			BlueLeftSample1=new Pose2d(48,24,Math.toRadians(0)),
			BlueLeftSample2=new Pose2d(44,22.5,Math.toRadians(0)),
			BlueLeftSample3=new Pose2d(55,22.5,Math.toRadians(0));
}
