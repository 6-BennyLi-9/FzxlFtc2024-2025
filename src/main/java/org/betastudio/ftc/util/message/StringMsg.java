package org.betastudio.ftc.util.message;

import androidx.annotation.NonNull;

import org.betastudio.ftc.ui.telemetry.TelemetryLine;

public class StringMsg extends LogMessage {
	private final String message;

	public StringMsg(final String message) {
		this.message = message;
	}

	@NonNull
	@Override
	public String toString() {
		return message;
	}

	@Override
	public TelemetryMsg buildTelemetryMsg() {
		return new TelemetryMsg(new TelemetryLine(message));
	}
}
