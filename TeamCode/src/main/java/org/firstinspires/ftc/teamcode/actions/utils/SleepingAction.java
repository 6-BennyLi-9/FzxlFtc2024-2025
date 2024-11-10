package org.firstinspires.ftc.teamcode.actions.utils;


import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.actions.Action;
import org.jetbrains.annotations.Contract;

public final class SleepingAction implements Action {
	private final long sleepMilliseconds;
	private double startTime;
	private boolean initialized=false;

	public SleepingAction(final long sleepMilliseconds){
		this.sleepMilliseconds = sleepMilliseconds;
	}

	@Override
	public boolean run() {
		if(!initialized){
			startTime=System.nanoTime()/ 1.0e6;
			initialized=true;
		}
		return System.nanoTime()/ 1.0e6 -startTime < sleepMilliseconds;
	}

	@NonNull
	@Contract(pure = true)
	@Override
	public String paramsString() {
		return sleepMilliseconds+"ms";
	}
}
