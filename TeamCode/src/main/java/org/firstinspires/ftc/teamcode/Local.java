package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import org.betastudio.ftc.Interfaces;
import org.firstinspires.ftc.teamcode.events.TaskCloseMonitor;

import java.util.concurrent.Callable;

public final class Local {
	public static void sleep(final long millis) {
		try {
			Thread.sleep(millis);
		} catch (final InterruptedException e) {
			if (Thread.currentThread() instanceof Interfaces.ThreadEx) {
				((Interfaces.ThreadEx) Thread.currentThread()).closeTask();
				new TaskCloseMonitor(Thread.currentThread());
			} else {
				Thread.currentThread().interrupt();
			}
		}
	}

	public static <K> void waitForVal(final Callable <K> function, final K expect) {
		waitForVal(function, expect, 60L);
	}

	public static <K> void waitForVal(final Callable <K> function, final K expect, final long flashMillis) {
		try {
			while (function.call() != expect) {
				sleep(flashMillis);
			}
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static <K> void waifForNotVal(final Callable <K> function, final K expect) {
		waifForNotVal(function, expect, 60L);
	}

	public static <K> void waifForNotVal(final Callable <K> function, final K expect, final long flashMillis){
		try {
			while (function.call() == expect) {
				sleep(flashMillis);
			}
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Deprecated
	public static void runMultiThreads(@NonNull final Runnable... threads) {
		for (final Runnable current : threads) {
			Global.service.execute(current);
		}
	}
}
