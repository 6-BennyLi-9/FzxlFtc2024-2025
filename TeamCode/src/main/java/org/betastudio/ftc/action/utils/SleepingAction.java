package org.betastudio.ftc.action.utils;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.ActionImpl;
import org.jetbrains.annotations.Contract;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public final class SleepingAction extends ActionImpl {
	private final long sleepMilliseconds;

	public SleepingAction(final long sleepMilliseconds) {
		final AtomicLong    startTime   = new AtomicLong();
		final AtomicBoolean initialized = new AtomicBoolean();
		this.sleepMilliseconds = sleepMilliseconds;
		setAction(() -> {
			if (! initialized.get()) {
				startTime.set(System.currentTimeMillis());
				initialized.set(true);
			}
			return System.currentTimeMillis() - startTime.get() <= sleepMilliseconds;
		});
	}

	@NonNull
	@Contract(pure = true)
	@Override
	public String paramsString() {
		return "t:" + sleepMilliseconds + "ms";
	}
}
