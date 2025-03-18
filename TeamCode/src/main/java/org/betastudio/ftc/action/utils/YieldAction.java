package org.betastudio.ftc.action.utils;

public class YieldAction extends  SleepingAction{
	public YieldAction(long sleepMilliseconds) {
		super(sleepMilliseconds);

		setName(getName() + this.getClass().getSimpleName());
	}

	@Override
	public boolean activate() {
		boolean activate = super.activate();
		Thread.yield();
		return activate;
	}
}
