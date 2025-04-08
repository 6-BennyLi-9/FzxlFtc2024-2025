package org.betastudio.ftc.action.utils;

import org.betastudio.ftc.action.ActionImplementFactory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class SleepingAction extends ActionImplementFactory {
	public SleepingAction(final long sleepMilliseconds) {
		final AtomicLong    startTime   = new AtomicLong();
		final AtomicBoolean initialized = new AtomicBoolean();
		setAction(() -> {
			if (! initialized.get()) {
				startTime.set(System.currentTimeMillis());
				initialized.set(true);
			}
			return System.currentTimeMillis() - startTime.get() <= sleepMilliseconds;
		});

		setParams(() -> "t:" + sleepMilliseconds + "ms");

		setName(getName() + this.getClass().getSimpleName());
	}
}
