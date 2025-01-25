package org.betastudio.ftc.util.message;

import androidx.annotation.NonNull;

public class StringMessage implements Message{
	private final String message;
	public StringMessage(final String message){
		this.message = message;
	}

	@NonNull
	@Override
	public String toString() {
		return message;
	}
}
