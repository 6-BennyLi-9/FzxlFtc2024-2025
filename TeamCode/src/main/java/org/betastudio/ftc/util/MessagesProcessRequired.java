package org.betastudio.ftc.util;

import androidx.annotation.NonNull;

import org.betastudio.ftc.message.Message;

import java.util.concurrent.Callable;

public interface MessagesProcessRequired <K extends Message> extends Callable<K> {
	void send(@NonNull K message);
	default K call() {return null;}
}
