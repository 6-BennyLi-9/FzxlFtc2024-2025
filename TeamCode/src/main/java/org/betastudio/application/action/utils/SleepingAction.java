package org.betastudio.application.action.utils;


import org.betastudio.application.action.Action;
import org.jetbrains.annotations.NotNull;

/**
 * 可以用于各种程序中方便地完成等待操作
 * <p>
 * 例如，你可以直接在代码中写：
 * {@code ... Actions.runAction(new SleepingAction(0.5)); ...}
 */
public final class SleepingAction implements Action {
	private final long    sleepMilliseconds;
	private       double  startTime;
	private       boolean initialized;

	public SleepingAction(final long sleepMilliseconds) {
		this.sleepMilliseconds = sleepMilliseconds;
	}

	@Override
	public boolean run() {
		if (! initialized) {
			startTime = System.nanoTime() / 1.0e6;
			initialized = true;
		}
		return System.nanoTime() / 1.0e6 - startTime < sleepMilliseconds;
	}

	@NotNull
	@Override
	public String paramsString() {
		return "t:" + sleepMilliseconds + "ms";
	}
}
