package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import org.betastudio.ftc.specification.ThreadEx;
import org.firstinspires.ftc.teamcode.events.TaskCloseMonitor;

import java.util.concurrent.Callable;

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
		waitForVal(function, expect, 60);
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

	public static void runMultiRunnable(@NonNull final Runnable... runnable) {
		for (final Runnable current : runnable) {
			Global.threadManager.add(new Thread(current));
		}
	}

	public static void runMultiThreads(@NonNull final Thread... threads) {
		for (final Thread current : threads) {
			Global.threadManager.add(current);
		}
	}
}
