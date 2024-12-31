package org.betastudio.ftc.client;

import org.betastudio.ftc.action.Action;

public class InfinityLoopAction implements Action {
	private final Runnable runnable;
	private       boolean  interrupted;

	InfinityLoopAction(final Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public boolean run() {
		runnable.run();
		return ! interrupted;
	}

	public void interrupt() {
		interrupted = true;
	}
}
