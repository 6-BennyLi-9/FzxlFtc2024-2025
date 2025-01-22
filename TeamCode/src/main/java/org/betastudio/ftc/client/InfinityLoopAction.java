package org.betastudio.ftc.client;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.util.ThreadEx;

public final class InfinityLoopAction implements Action, ThreadEx {
	private final Runnable runnable;
	private       boolean  interrupted;

	public InfinityLoopAction(final Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public boolean run() {
		runnable.run();
		return ! interrupted;
	}

	@Override
	public void closeTask() {
		interrupted = true;
	}
}
