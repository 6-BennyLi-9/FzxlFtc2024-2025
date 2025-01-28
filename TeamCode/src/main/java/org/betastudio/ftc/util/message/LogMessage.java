package org.betastudio.ftc.util.message;

public abstract class LogMessage implements Message {
	public abstract TelemetryMsg buildTelemetryMsg();
}
