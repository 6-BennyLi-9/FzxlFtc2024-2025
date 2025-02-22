package org.exboithpath.graphics;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.jetbrains.annotations.Contract;

public class Pose extends Vec{
	public final double heading;

	public Pose(double x, double y, double heading) {
		super(x,y);
		this.heading = heading;
	}

	public Pose plus(@NonNull Pose arg){
		return new Pose(x+arg.x,y+arg.y,heading+arg.heading);
	}
	public Pose minus(@NonNull Pose arg){
		return new Pose(x-arg.x,y-arg.y,heading-arg.heading);
	}
	public Pose times(@NonNull Pose arg){
		return new Pose(x*arg.x,y*arg.y,heading*arg.heading);
	}
	public Pose poseTrackTo(@NonNull Pose target){
		Pose delta = target.minus(this);
		double distance = delta.dis();
		double angle = Math.atan2(delta.y,delta.x);
		return new Pose(distance*Math.cos(angle),distance*Math.sin(angle),angle);
	}

	public Pose2d toPose2d(){
		return new Pose2d(x,y,heading);
	}
	@NonNull
	@Contract("_ -> new")
	public static Pose poseOf(@NonNull Pose2d arg){
		return new Pose(arg.getX(),arg.getY(),arg.getHeading());
	}
}
