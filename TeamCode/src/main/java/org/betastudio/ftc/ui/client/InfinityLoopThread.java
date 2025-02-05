package org.betastudio.ftc.ui.client;

import org.betastudio.ftc.util.entry.ThreadEx;

public class InfinityLoopThread extends Thread implements ThreadEx {
	private final InfinityLoopAction action;

	public InfinityLoopThread(final Runnable runnable) {
		action=new InfinityLoopAction(runnable);
	}

	@Override
	public void run() {
		action.run();
	}

	@Override
	public void closeTask() {
		action.closeTask();
	}
}
