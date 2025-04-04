package org.betastudio.ftc.thread;

import org.betastudio.ftc.Annotations;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Annotations.Beta(date = "25.3.11")
public class ObjectTransportStream <T> {
	public final Lock    pushLock;
	public final Lock    receiveLock;
	public boolean isValuePushed;
	public T       value;

	public ObjectTransportStream() {
		this(null);
	}

	public ObjectTransportStream(final T initialValue) {
		this.value = initialValue;
		pushLock = new ReentrantLock();
		receiveLock = new ReentrantLock();
	}

	public void pushValue(final T value) {
		pushValue(value, TimeUnit.SECONDS, 1L);
	}

	public void pushValue(final T value, final TimeUnit unit, final long timeout){
		pushLock.lock();
		final long startTime = System.nanoTime();
		while (isValuePushed) {
			if (System.nanoTime() - startTime >= unit.toNanos(timeout)){
				return;
			}
			Thread.yield();
		}

		this.value = value;
		isValuePushed = true;
		pushLock.unlock();
	}

	public T receiveValue() {
		return receiveValue(TimeUnit.SECONDS, 1L, null);
	}

	public T receiveValue(final T defaultValue) {
		return receiveValue(TimeUnit.SECONDS, 1L, defaultValue);
	}

	public T receiveValue(final TimeUnit unit, final long timeout) {
		return receiveValue(unit, timeout, null);
	}

	public T receiveValue(final TimeUnit unit, final long timeout, final T defaultValue){
		receiveLock.lock();
		final long startTime = System.nanoTime();
		while (!isValuePushed) {
			if (System.nanoTime() - startTime >= unit.toNanos(timeout)){
				receiveLock.unlock();
				return defaultValue;
			}
			Thread.yield();
		}

		isValuePushed = false;
		receiveLock.unlock();
		return this.value;
	}

	public T peakValue(){
		return this.value;
	}
}
