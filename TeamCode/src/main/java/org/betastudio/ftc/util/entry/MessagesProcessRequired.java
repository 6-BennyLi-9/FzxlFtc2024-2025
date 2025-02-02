package org.betastudio.ftc.util.entry;

import androidx.annotation.NonNull;

import org.betastudio.ftc.util.message.Message;

public interface MessagesProcessRequired<K extends Message> {
	void sendMsg(@NonNull K message);

	default K callMsg() {
		return null;
	}
}
