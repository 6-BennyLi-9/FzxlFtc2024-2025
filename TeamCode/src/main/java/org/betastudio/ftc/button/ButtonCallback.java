package org.betastudio.ftc.button;

@FunctionalInterface
public interface ButtonCallback {
	void onActive();

	default void onDisabled() {
	}
}
