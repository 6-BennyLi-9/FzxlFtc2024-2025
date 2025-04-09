package org.betastudio.ftc.thread;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.action.Actions;

public class InfinityLoopThread extends Thread implements Interfaces.ThreadEx {
	private final InfinityLoopAction action;

	public InfinityLoopThread(final Runnable runnable) {
		action = new InfinityLoopAction(runnable);
	}

	@Override
	public void run() {
		Actions.runAction(action);
	}

	@Override
	public void closeTask() {
		action.closeTask();
	}

}
