package org.betastudio.ftc.action.utils;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.ActionImpl;
import org.betastudio.ftc.util.time.Timer;
import org.jetbrains.annotations.Contract;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class SleepingAction extends ActionImpl {
	private final long                               sleepMilliseconds;
	private final AtomicReference <Double>           startTime   = new AtomicReference<>(0.0);
	private final AtomicBoolean                      initialized = new AtomicBoolean(false);

	public SleepingAction(final long sleepMilliseconds) {
		final AtomicReference <Callable <Boolean>> actionRef = new AtomicReference <>();
		actionRef.set(() -> Timer.getCurrentTime() - startTime.get() >= sleepMilliseconds);
		final Callable <Boolean> action = actionRef.get();
		this.sleepMilliseconds = sleepMilliseconds;

		setAction(action);
	}

	@NonNull
	@Contract(pure = true)
	@Override
	public String paramsString() {
		return "t:" + sleepMilliseconds + "ms";
	}
}
