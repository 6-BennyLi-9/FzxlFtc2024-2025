package org.betastudio.ftc.message;

import androidx.annotation.NonNull;

import org.betastudio.ftc.ui.telemetry.TelemetryLine;

import java.util.Objects;

public final class LogMessages {
	public abstract static class LogMessage implements Message {
		public abstract TelemetryMsg buildTelemetryMsg();
	}

	public static class ExceptionMsg extends LogMessage {
		private final Throwable exception;

		public ExceptionMsg(final Throwable exception) {
			this.exception = exception;
		}

		@NonNull
		@Override
		public String toString() {
			return "error:" + "\"" + Objects.requireNonNullElse(exception.getMessage(), "*nullptr*") + "\"";
		}

		public TelemetryMsg buildTelemetryMsg() {
			final TelemetryMsg result = new TelemetryMsg(), packages = new TelemetryMsg();
			for (final StackTraceElement element : exception.getStackTrace()) {
				result.add(new TelemetryLine("at:" + element.getFileName()));
				result.add(new TelemetryLine(" -method:" + element.getMethodName()));
				result.add(new TelemetryLine(" -line:" + element.getLineNumber()));
				packages.add(new TelemetryLine("~package class:" + element.getClassName()));
			}
			result.merge(packages);
			return result;
		}
	}

	public static class StringMsg extends LogMessage {
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
}
