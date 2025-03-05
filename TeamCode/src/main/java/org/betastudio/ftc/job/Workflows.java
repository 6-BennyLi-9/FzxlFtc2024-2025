package org.betastudio.ftc.job;

import androidx.annotation.NonNull;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.Actions;
import org.betastudio.ftc.job.render.IgnoredJobProgressRender;

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

	public static void activeJob(Job arg){
		Actions.runAction(() -> activeJobSync(arg));
	}

	public static void activeJob(Job arg, Interfaces.JobProgressRender render){
		Actions.runAction(() -> activeJobSync(arg, render));
	}

	public static boolean activeJobSync(@NonNull Job arg) {
		return activeJobSync(arg, new IgnoredJobProgressRender());
	}

	public static boolean activeJobSync(@NonNull Job arg, @NonNull Interfaces.JobProgressRender render){
		boolean res = arg.activate();
		if (arg instanceof RenderedJob){
			render.render(((RenderedJob) arg).value());
		}
		return res;
	}
}
