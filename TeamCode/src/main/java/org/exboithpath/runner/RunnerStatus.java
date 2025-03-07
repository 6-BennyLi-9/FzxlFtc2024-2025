package org.exboithpath.runner;

public enum RunnerStatus {
	STARTING(true),
	RUNNING(false),
	STOPPING(true),
	CALIBRATING(false),
	IDLE(false);
	public final boolean isJiffy;

	RunnerStatus(boolean isJiffy) {
		this.isJiffy = isJiffy;
	}
}
