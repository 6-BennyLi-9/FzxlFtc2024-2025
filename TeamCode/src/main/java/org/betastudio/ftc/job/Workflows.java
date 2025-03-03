package org.betastudio.ftc.job;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;

public final class Workflows {
	@NonNull
	public static Job newSteppedJob(String name, Action action) {
		Job res = new Step(action);
		res.setName(name);
		return res;
	}

	@NonNull
	public static Job newSteppedJob(String name, Runnable work) {
		Job res = new Step(work);
		res.setName(name);
		return res;
	}
}
