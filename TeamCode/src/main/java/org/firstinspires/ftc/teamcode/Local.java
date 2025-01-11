package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import org.betastudio.ftc.interfaces.ThreadAdditions;
import org.firstinspires.ftc.teamcode.events.TaskCloseMonitor;

import java.util.concurrent.Callable;

public final class Local {
	public static void sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ignored) {
			if (Thread.currentThread() instanceof ThreadAdditions){
				((ThreadAdditions) Thread.currentThread()).closeTask();
				new TaskCloseMonitor(Thread.currentThread());
			}else{
				Thread.currentThread().interrupt();
			}
		}
	}

	public static <K> void waitForVal(Callable<K> function, K expect){
		waitForVal (function,expect,60);
	}
	public static <K> void waitForVal(Callable<K> function, K expect, long flashMillis){
		try {
			while (function.call()!=expect){
				sleep(flashMillis);
			}
		}catch (Exception ignored){}
	}

	public static void runMultiRunnable(@NonNull Runnable... runnable){
		for(Runnable current : runnable){
			Global.threadManager.add(new Thread(current));
		}
	}
	public static void runMultiThreads(@NonNull Thread... threads){
		for (Thread current : threads){
			Global.threadManager.add(current);
		}
	}
}
