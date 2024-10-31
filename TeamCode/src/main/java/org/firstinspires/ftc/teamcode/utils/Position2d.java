package org.firstinspires.ftc.teamcode.utils;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;

import org.jetbrains.annotations.Contract;

public final class Position2d {
	public double x,y, heading;
	public Position2d(double x, double y, double heading){
		this.x=x;
		this.y=y;
		this.heading = heading;
	}
	public Position2d(@NonNull Vector2d pose, double heading){
		this(pose.x,pose.y,heading);
	}

	@NonNull
	@Contract(" -> new")
	public Pose2d toPose2d(){
		return new Pose2d(x,y,heading);
	}
	@NonNull
	@Contract(" -> new")
	public Vector2d toVector(){
		return new Vector2d(x,y);
	}

	@NonNull
	@Override
	public String toString() {
		return "("+x+","+y+"):"+heading;
	}

	@NonNull
	@Contract("_ -> new")
	public Vector2d minus(@NonNull Vector2d pose) {
		return new Vector2d(x-pose.x,y- pose.y);
	}

	@NonNull
	@Contract("_ -> new")
	public Vector2d plus(@NonNull Vector2d pose) {
		return new Vector2d(x+ pose.x,y+ pose.y);
	}
}