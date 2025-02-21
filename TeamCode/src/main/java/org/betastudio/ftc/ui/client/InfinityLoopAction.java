package org.betastudio.ftc.ui.client;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.util.entry.ThreadEx;

public final class InfinityLoopAction implements Action, ThreadEx {
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
