package org.betastudio.ftc.util.entry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.betastudio.ftc.util.message.Message;

public interface MessagesProcessRequired<K extends Message> {
	void sendMsg(@NonNull final K message);

	@Nullable
	default K callMsg() {
		return null;
	}
}
