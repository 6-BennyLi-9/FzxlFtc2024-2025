package org.betastudio.ftc.thread;

import static org.betastudio.ftc.Interfaces.ThreadEx;
import static org.betastudio.ftc.util.ExceptionsUtil.getOriginException;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class MethodFrequencyCaller implements ThreadEx, Runnable {
	protected final Runnable           methodCall;
	protected       Callable <Boolean> isStopRequested = () -> false;
	protected       long               FPS             = 10;

	public MethodFrequencyCaller(final Runnable methodCall) {
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
			} catch (final Exception e) {
				Throwable cause = getOriginException(e);
				throw new RuntimeException(cause);
			}
			methodCall.run();
		}
	}

	@Override
	public void closeTask() {
		isStopRequested = () -> true;
	}

	public void setStopRequestCaller(final Callable <Boolean> isStopRequested) {
		this.isStopRequested = isStopRequested;
	}

	public void setFrequencyFPS(final long FPS) {
		this.FPS = FPS;
	}
}
