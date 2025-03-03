package org.betastudio.ftc.job;

public class JobNotParalleledException extends RuntimeException {
	public JobNotParalleledException() {
		super();
	}
	public JobNotParalleledException(String message) {
		super(message);
	}
}
