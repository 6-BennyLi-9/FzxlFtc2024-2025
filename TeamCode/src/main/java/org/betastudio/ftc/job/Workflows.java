package org.betastudio.ftc.job;

import static org.betastudio.ftc.Annotations.MirrorMethod;

import androidx.annotation.NonNull;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.Actions;
import org.betastudio.ftc.action.utils.StatementAction;
import org.betastudio.ftc.job.implementation.Step;
import org.betastudio.ftc.job.render.IgnoredProgressRender;

import java.util.Objects;

public final class Workflows {
	@NonNull
	public static Step newSteppedJob(String name, Action action) {
		Step res = new Step(action);
		res.setName(name);
		return res;
	}

	@MirrorMethod
	@NonNull
	public static Step newSteppedJob(String name, Runnable work) {
		return newSteppedJob(name, new StatementAction(work));
	}

	public static void activeJob(Job arg){
		Actions.runAction(() -> activeJobSync(arg));
	}

	public static void activeJob(Job arg, Interfaces.ProgressRender render){
		Actions.runAction(() -> activeJobSync(arg, render));
	}

	@MirrorMethod
	public static boolean activeJobSync(@NonNull Job arg) {
		return activeJobSync(arg, new IgnoredProgressRender());
	}

	public static boolean activeJobSync(@NonNull Job arg, @NonNull Interfaces.ProgressRender render){
		synchronized (Objects.requireNonNull(arg)) {
			boolean res = arg.activate();
			if (arg instanceof RenderedJob) {
				render.render(arg.getName(), ((RenderedJob) arg).getVal());
			}
			return res;
		}
	}
}
