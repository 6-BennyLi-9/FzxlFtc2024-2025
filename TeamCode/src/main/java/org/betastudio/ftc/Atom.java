package org.betastudio.ftc;

import androidx.annotation.Nullable;

public class Atom<T>{
	private T val;

	public Atom(){
	}
	public Atom(T val){
		this.val = val;
	}

	public void set(T val) {
		this.val = val;
	}

	public T get() {
		return val;
	}

	public T setAndGet(T val){
		return this.val = val;
	}

	public T getAndSet(T val){
		T oldVal = this.val;
		this.val = val;
		return oldVal;
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		assert obj instanceof Atom;
		return val.equals(obj);
	}
}
