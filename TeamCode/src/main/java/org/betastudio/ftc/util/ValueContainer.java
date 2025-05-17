package org.betastudio.ftc.util;

public final class ValueContainer <T> {
	private T v;

	public ValueContainer(T initial){
		v=initial;
	}

	public T getV() {
		return v;
	}

	public void setV(T v) {
		this.v = v;
	}
}
