package org.betastudio.ftc.message;

public abstract class LogMessage implements Message {
	public abstract TelemetryMsg buildTelemetryMsg();
}
