package org.betastudio.ftc.thread;

import org.betastudio.ftc.Interfaces;

public class MethodFrequencyCaller extends Thread implements Interfaces.ThreadEx {
	protected final Runnable methodCall;
	protected       boolean  isStopRequested;

	public MethodFrequencyCaller(Runnable methodCall) {
		this.methodCall = methodCall;
	}

	/// 不要直接调用
	@Override
	public void run() {
		while (! isStopRequested) {
			methodCall.run();
		}
	}

	@Override
	public void closeTask() {
		isStopRequested = true;
	}
}
