package org.betastudio.ftc.thread;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.action.Action;

public final class InfinityLoopAction implements Action, Interfaces.ThreadEx {
	private final Runnable runnable;
	private       boolean  interrupted;

	public InfinityLoopAction(final Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public boolean activate() {
		runnable.run();
		return ! interrupted;
	}

	@Override
	public void closeTask() {
		interrupted = true;
	}
}
