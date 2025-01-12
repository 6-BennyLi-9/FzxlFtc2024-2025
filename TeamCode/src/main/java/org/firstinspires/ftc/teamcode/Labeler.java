package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

public class Labeler {
	private long ID;

	public long summonID(){
		++ID;
		return ID;
	}
	public String summonID(@NonNull final Object object){
		return object.getClass().getSimpleName()+":"+summonID();
	}
}
