package org.betastudio.ftc.thread;

import static org.betastudio.ftc.Interfaces.ThreadEx;

import org.betastudio.ftc.action.Actions;

public class InfinityLoopThread extends Thread implements ThreadEx {
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
