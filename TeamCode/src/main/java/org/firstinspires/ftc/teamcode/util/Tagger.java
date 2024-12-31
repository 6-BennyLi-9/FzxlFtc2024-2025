package org.firstinspires.ftc.teamcode.util;

import androidx.annotation.NonNull;

public class Tagger {
	private long ID;

	public long summonID(){
		return ++ID;
	}
	public String summonID(@NonNull Object object){
		return object.getClass().getSimpleName()+":"+summonID();
	}
}
