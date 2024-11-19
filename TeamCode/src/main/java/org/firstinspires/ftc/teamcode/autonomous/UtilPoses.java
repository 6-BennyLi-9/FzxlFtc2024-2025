package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;

//TODO 转为 static class
public enum UtilPoses {
	//起始点位
	BlueLeftStart(new Pose2d(12,62,Math.toRadians(-90))),
	BlueRightStart(new Pose2d(-12,62,Math.toRadians(-90))),
	RedLeftStart(new Pose2d(-12,-62,Math.toRadians(90))),
	RedRightStart(new Pose2d(12,-62,Math.toRadians(90))),

	BlueDecant(new Pose2d(55,55,Math.toRadians(-135))),
	RedDecant(new Pose2d(-55,-55,Math.toRadians(135))),

	BlueLeftSample1(new Pose2d(32,22.5,Math.toRadians(0))),
	BlueLeftSample2(new Pose2d(44,22.5,Math.toRadians(0))),
	BlueLeftSample3(new Pose2d(55,22.5,Math.toRadians(0))),


	;
	public final Pose2d pose;
	UtilPoses(final Pose2d pose){
		this.pose=pose;
	}
}
