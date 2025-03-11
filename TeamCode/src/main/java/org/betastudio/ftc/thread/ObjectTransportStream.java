package org.betastudio.ftc.thread;

import org.betastudio.ftc.Annotations;

import java.util.concurrent.TimeUnit;

@Annotations.Beta(date = "25.3.11")
public class ObjectTransportStream <T> {
	public boolean isValuePushed;
	public T       value;

	public ObjectTransportStream() {
		this(null);
	}

	public ObjectTransportStream(T initialValue) {
		this.value = initialValue;
	}

	public void pushValue(T value) {
		pushValue(value, TimeUnit.SECONDS, 1L);
	}

	public void pushValue(T value, TimeUnit unit, long timeout){
		long startTime = System.nanoTime();
		while (isValuePushed) {
			if (System.nanoTime() - startTime >= unit.toNanos(timeout)){
				return;
			}
			Thread.yield();
		}

		this.value = value;
		isValuePushed = true;
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
		long startTime = System.nanoTime();
		while (!isValuePushed) {
			if (System.nanoTime() - startTime >= unit.toNanos(timeout)){
				return defaultValue;
			}
			Thread.yield();
		}

		isValuePushed = false;
		return this.value;
	}

	public T peakValue(){
		return this.value;
	}
}
