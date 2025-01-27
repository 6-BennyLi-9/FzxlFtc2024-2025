package org.betastudio.ftc.util.message;

import androidx.annotation.NonNull;

import org.betastudio.ftc.ui.telemetry.TelemetryLine;

import java.util.Objects;

public class ExceptionMsg extends LogMessage{
	private final Throwable exception;
	public ExceptionMsg(final Throwable exception) {
		this.exception = exception;
	}

	@NonNull
	@Override
	public String toString() {
		return "error:"+"\""+Objects.requireNonNull(exception.getMessage())+"\"";
	}

	public TelemetryMsg buildTelemetryMsg() {
		final TelemetryMsg result = new TelemetryMsg();
		for (StackTraceElement element : exception.getStackTrace()) {
			result.add(new TelemetryLine(element.getClassName() + "@" + element.getMethodName() + "():" + element.getLineNumber()));
		}
		return result;
	}
}
