package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

public class Labeler {
	private long ID;

	public long summonID(){
		return ++ID;
	}
	public String summonID(@NonNull Object object){
		return object.getClass().getSimpleName()+":"+summonID();
	}
}
