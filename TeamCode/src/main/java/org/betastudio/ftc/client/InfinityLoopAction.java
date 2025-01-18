package org.betastudio.ftc.client;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.interfaces.ThreadAdditions;

public final class InfinityLoopAction implements Action, ThreadAdditions {
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
