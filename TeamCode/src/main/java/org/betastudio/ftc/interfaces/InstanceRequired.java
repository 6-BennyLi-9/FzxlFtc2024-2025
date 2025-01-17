package org.betastudio.ftc.interfaces;

@Deprecated
public interface InstanceRequired<K> {
	K getInstance();
	void setInstance(K instance);
}
