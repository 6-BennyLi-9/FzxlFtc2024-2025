package org.exboithpath.graphics;

import androidx.annotation.NonNull;

public class Vec {
	public final double x,y;

	public Vec(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vec plus(@NonNull Vec arg){
		return new Vec(x+arg.x,y+arg.y);
	}
	public Vec minus(@NonNull Vec arg){
		return new Vec(x-arg.x,y-arg.y);
	}
	public Vec times(@NonNull Vec arg){
		return new Vec(x*arg.x,y*arg.y);
	}
	public strictfp double dis(){
		return Math.sqrt(x*x+y*y);
	}
	public strictfp double disTo(@NonNull Vec arg) {
		return Math.sqrt((x - arg.x) * (x - arg.x) + (y - arg.y) * (y - arg.y));
	}

	@NonNull
	@Override
	public String toString() {
		return "Vec{" + "x=" + x + ", y=" + y + '}';
	}
}
