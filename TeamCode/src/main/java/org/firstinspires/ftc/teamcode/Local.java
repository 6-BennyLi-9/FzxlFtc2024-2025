package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import org.betastudio.ftc.interfaces.ThreadAdditions;
import org.firstinspires.ftc.teamcode.events.TaskCloseMonitor;

import java.util.concurrent.Callable;

public enum Local {
	;

	public static void sleep(final long millis){
		try {
			Thread.sleep(millis);
		} catch (final InterruptedException ignored) {
			if (Thread.currentThread() instanceof ThreadAdditions){
				((ThreadAdditions) Thread.currentThread()).closeTask();
				new TaskCloseMonitor(Thread.currentThread());
			}else{
				Thread.currentThread().interrupt();
			}
		}
	}

	public static <K> void waitForVal(final Callable<K> function, final K expect){
		waitForVal (function,expect,60);
	}
	public static <K> void waitForVal(final Callable<K> function, final K expect, final long flashMillis){
		try {
			while (function.call()!=expect){
				sleep(flashMillis);
			}
		}catch (final Exception ignored){}
	}

	public static void runMultiRunnable(@NonNull final Runnable... runnable){
		for(final Runnable current : runnable){
			Global.threadManager.add(new Thread(current));
		}
	}
	public static void runMultiThreads(@NonNull final Thread... threads){
		for (final Thread current : threads){
			Global.threadManager.add(current);
		}
	}
}
