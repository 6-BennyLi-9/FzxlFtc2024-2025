package org.exboithpath.graphics;

import androidx.annotation.NonNull;

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
	public strictfp double dis(){
		return Math.sqrt(x*x+y*y);
	}
}
