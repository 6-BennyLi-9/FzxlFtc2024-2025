package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import java.util.concurrent.Callable;

public class Local {
	public static void sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ignored) {}
	}

	public static <K> void waitForVal(Callable<K> function, K expect){
		waitForVal (function,expect,60);
	}
	public static <K> void waitForVal(Callable<K> function, K expect, long flashMillis){
		try {
			while (function.call()!=expect){
				sleep(flashMillis);
			}
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	public static void runMultiRunnable(@NonNull Runnable... runnable){
		for(Runnable current : runnable){
			Global.coreThreads.add(new Thread(current));
		}
	}
	public static void runMultiThreads(@NonNull Thread... threads){
		for (Thread current : threads){
			Global.coreThreads.add(current);
		}
	}
}
