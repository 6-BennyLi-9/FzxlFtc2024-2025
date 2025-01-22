package org.betastudio.ftc;

import androidx.annotation.Nullable;

public class Atom<T>{
	private T val;

	public Atom(){
	}
	public Atom(final T val){
		this.val = val;
	}

	public void set(final T val) {
		this.val = val;
	}

	public T get() {
		return val;
	}

	public T setAndGet(final T val){
		return this.val = val;
	}

	@Deprecated
	public T getAndSet(final T val){
		final T oldVal = this.val;
		this.val = val;
		return oldVal;
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		assert obj instanceof Atom;
		return val.equals(obj);
	}
}
