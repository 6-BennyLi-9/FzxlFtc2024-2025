package org.betastudio.ftc.ui.telemetry;

import androidx.annotation.NonNull;

import org.betastudio.ftc.ui.log.FtcLogElement;

public class LogTelemetryItem extends TelemetryItem {
	private final FtcLogElement logElement;
	public LogTelemetryItem(String capital, String value, FtcLogElement logElement) {
		super(capital, value);
		this.logElement = logElement;
	}

	public LogTelemetryItem(String capital, @NonNull Object value,	FtcLogElement logElement) {
		super(capital, value);
		this.logElement = logElement;
	}

	public FtcLogElement getLogElement() {
		return logElement;
	}
}
