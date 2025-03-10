package org.betastudio.ftc.thread;

import java.util.concurrent.Callable;

public abstract class RunnableCallable <T> implements Runnable, Callable<T> {
	private T result;

	@Override
	public void run() {
		if(null == result){
			result = call();
		}
	}

	@Override
	public abstract T call();
}
