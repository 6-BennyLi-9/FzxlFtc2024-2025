package org.betastudio.ftc.util.message;

import androidx.annotation.NonNull;

public class StringMsg implements Message{
	private final String message;
	public StringMsg(final String message){
		this.message = message;
	}

	@NonNull
	@Override
	public String toString() {
		return message;
	}
}
