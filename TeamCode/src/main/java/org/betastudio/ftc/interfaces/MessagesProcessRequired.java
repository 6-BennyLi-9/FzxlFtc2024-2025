package org.betastudio.ftc.interfaces;

import androidx.annotation.NonNull;

import java.util.concurrent.Callable;

public interface MessagesProcessRequired <K> extends Callable<K> {
	void sendRequest(@NonNull K message);
	default K call() {return null;}
}
