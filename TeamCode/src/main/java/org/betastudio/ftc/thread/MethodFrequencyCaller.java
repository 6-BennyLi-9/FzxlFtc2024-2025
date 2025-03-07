package org.betastudio.ftc.thread;

import org.betastudio.ftc.Interfaces;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class MethodFrequencyCaller implements Interfaces.ThreadEx, Runnable {
	protected final Runnable           methodCall;
	protected       Callable <Boolean> isStopRequested = () -> false;
	protected       long               FPS             = 10;

	public MethodFrequencyCaller(Runnable methodCall) {
		this.methodCall = methodCall;
	}

	/// 不要直接调用
	@Override
	public void run() {
		while (true) {
			try {
				if (isStopRequested.call()) {
					break;
				}
				TimeUnit.MILLISECONDS.sleep(1000 / FPS);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			methodCall.run();
		}
	}

	@Override
	public void closeTask() {
		isStopRequested = () -> true;
	}

	public void setRequestCaller(Callable <Boolean> isStopRequested) {
		this.isStopRequested = isStopRequested;
	}

	public void setFrequencyFPS(final long FPS) {
		this.FPS = FPS;
	}
}
