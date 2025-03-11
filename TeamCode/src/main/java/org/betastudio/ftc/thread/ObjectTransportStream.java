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

	public ObjectTransportStream(T initialValue) {
		this.value = initialValue;
		pushLock = new ReentrantLock();
		receiveLock = new ReentrantLock();
	}

	public void pushValue(T value) {
		pushValue(value, TimeUnit.SECONDS, 1L);
	}

	public void pushValue(T value, TimeUnit unit, long timeout){
		pushLock.lock();
		long startTime = System.nanoTime();
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

	public T receiveValue(T defaultValue) {
		return receiveValue(TimeUnit.SECONDS, 1L, defaultValue);
	}

	public T receiveValue(TimeUnit unit, long timeout) {
		return receiveValue(unit, timeout, null);
	}

	public T receiveValue(TimeUnit unit, long timeout, T defaultValue){
		receiveLock.lock();
		long startTime = System.nanoTime();
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
