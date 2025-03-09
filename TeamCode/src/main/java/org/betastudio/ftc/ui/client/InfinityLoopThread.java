package org.betastudio.ftc.ui.client;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.action.Actions;

public class InfinityLoopThread extends Thread implements Interfaces.ThreadEx {
	private final InfinityLoopAction action;

	public InfinityLoopThread(final Runnable runnable) {
		action=new InfinityLoopAction(runnable);
	}

	@Override
	public void run() {
		Actions.runAction(action);
	}

	@Override
	public void closeTask() {
		action.closeTask();
	}

	public static final class InfinityLoopAction implements Action, Interfaces.ThreadEx {
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
}
