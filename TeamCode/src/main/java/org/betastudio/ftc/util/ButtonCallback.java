package org.betastudio.ftc.util;

@FunctionalInterface
public interface ButtonCallback {
	void onActive();

	default void onDisabled() {
	}
}
