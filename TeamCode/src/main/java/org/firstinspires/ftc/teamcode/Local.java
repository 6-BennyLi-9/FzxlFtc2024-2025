package org.firstinspires.ftc.teamcode;

import static org.betastudio.ftc.Interfaces.ThreadEx;
import static org.betastudio.ftc.util.ExceptionsUtil.getOriginException;

import org.firstinspires.ftc.teamcode.events.TaskCloseMonitor;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class Local {
	public static void sleep(final long millis) {
		try {
			Thread.sleep(millis);
		} catch (final InterruptedException e) {
			if (Thread.currentThread() instanceof ThreadEx) {
				((ThreadEx) Thread.currentThread()).closeTask();
				new TaskCloseMonitor(Thread.currentThread());
			} else {
				Thread.currentThread().interrupt();
			}
		}
	}

	public static <K> void waitForVal(final Callable <K> function, final K expect) {
		waitForVal(function, expect, TimeUnit.MILLISECONDS, - 1);
	}

	public static <K> void waitForVal(final Callable <K> function, final K expect, final TimeUnit unit, final long timeout) {
		final AtomicBoolean timeLimited = new AtomicBoolean(false);
		if (- 1 != timeout) {
			Global.service.execute(() -> {
				try {
					unit.sleep(timeout);
				} catch (final InterruptedException e) {
					Thread.currentThread().interrupt();
				}

				timeLimited.set(true);
			});
		}
		try {
			while (function.call() != expect && ! timeLimited.get()) {
				Thread.yield();
			}
		} catch (final Exception e) {
			Throwable cause = getOriginException(e);
			throw new RuntimeException(cause);
		}
	}

	public static <K> void waifForNotVal(final Callable <K> function, final K expect) {
		waifForNotVal(function, expect, 60L);
	}

	public static <K> void waifForNotVal(final Callable <K> function, final K expect, final long flashMillis) {
		try {
			while (function.call() == expect) {
				sleep(flashMillis);
			}
		} catch (final Exception e) {
			Throwable cause = getOriginException(e);
			throw new RuntimeException(cause);
		}
	}

}
