package org.betastudio.ftc.util.message;

import androidx.annotation.NonNull;

import java.util.Objects;

public class ExceptionMsg implements Message{
	private final Throwable exception;
	public ExceptionMsg(final Throwable exception) {
		this.exception = exception;
	}

	@NonNull
	@Override
	public String toString() {
		return Objects.requireNonNull(exception.getMessage());
	}
}
