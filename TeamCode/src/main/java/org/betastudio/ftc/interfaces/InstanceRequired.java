package org.betastudio.ftc.interfaces;

public interface InstanceRequired<K> {
	K getInstance();
	void setInstance(K instance);
}
