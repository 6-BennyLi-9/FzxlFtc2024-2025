package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;

public enum UtilPoses {
	BlueLeftStart(new Pose2d(0,0))
	;
	public final Pose2d pose;
	UtilPoses(final Pose2d pose){
		this.pose=pose;
	}
}
