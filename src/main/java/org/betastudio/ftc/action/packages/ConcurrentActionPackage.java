package org.betastudio.ftc.action.packages;

import org.betastudio.ftc.Annotations.Beta;
import org.betastudio.ftc.thread.RunnableCallable;

@Beta(date = "2025-2-28")
public class ConcurrentActionPackage extends TaggedActionPackage {
	public static abstract class ActionTask extends RunnableCallable<Boolean> {
	}

	public ActionTask newActivateTask() {
		return new ActionTask() {
			@Override
			public Boolean call() {
				return activate();
			}
		};
	}

	public Runnable newActivateTillEndTask() {
		return this::activateTillEnd;
	}
}
