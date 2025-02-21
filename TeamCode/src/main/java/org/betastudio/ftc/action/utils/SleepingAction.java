package org.betastudio.ftc.action.utils;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.ActionImpl;
import org.betastudio.ftc.time.Timer;
import org.jetbrains.annotations.Contract;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class SleepingAction extends ActionImpl {
	private final long sleepMilliseconds;

	public SleepingAction(final long sleepMilliseconds) {
		final AtomicReference <Double> startTime   = new AtomicReference <>(0.0);
		final AtomicBoolean            initialized = new AtomicBoolean(false);
		this.sleepMilliseconds = sleepMilliseconds;
		setAction(() -> {
			if (! initialized.get()) {
				startTime.set(Timer.getCurrentTime());
				initialized.set(true);
			}
			return Timer.getCurrentTime() - startTime.get() >= sleepMilliseconds;
		});
	}

	@NonNull
	@Contract(pure = true)
	@Override
	public String paramsString() {
		return "t:" + sleepMilliseconds + "ms";
	}
}
