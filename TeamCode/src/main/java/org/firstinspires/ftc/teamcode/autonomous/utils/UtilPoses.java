package org.firstinspires.ftc.teamcode.autonomous.utils;

import com.acmerobotics.roadrunner.geometry.Pose2d;

@SuppressWarnings("unused")
public enum UtilPoses {
	;
	//起始点位
	public static Pose2d BlueLeftStart=new Pose2d(12,62,Math.toRadians(-90));
	public static Pose2d BlueRightStart=new Pose2d(-12,62,Math.toRadians(-90));
	public static Pose2d RedLeftStart=new Pose2d(-12,-62,Math.toRadians(90));
	public static Pose2d RedRightStart=new Pose2d(12,-62,Math.toRadians(90));

	public static Pose2d BlueDecant=new Pose2d(55,55,Math.toRadians(-135));
	public static Pose2d RedDecant=new Pose2d(-55,-55,Math.toRadians(135));

	public static Pose2d BlueLeftSuspend=new Pose2d(BlueLeftStart.getX(),32,Math.toRadians(90));

	public static Pose2d BlueLeftSample1=new Pose2d(48,24,Math.toRadians(0));
	public static Pose2d BlueLeftSample2=new Pose2d(44,22.5,Math.toRadians(0));
	public static Pose2d BlueLeftSample3=new Pose2d(55,22.5,Math.toRadians(0));
}
