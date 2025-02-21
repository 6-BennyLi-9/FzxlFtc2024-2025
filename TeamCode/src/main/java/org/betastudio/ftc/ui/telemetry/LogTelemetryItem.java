package org.betastudio.ftc.ui.telemetry;

import androidx.annotation.NonNull;

import org.betastudio.ftc.ui.log.FtcLogElement;

public class LogTelemetryItem extends TelemetryItem {
	private final FtcLogElement logElement;

	public LogTelemetryItem(final String capital, final String value, final FtcLogElement logElement) {
		super(capital, value);
		this.logElement = logElement;
	}

	public LogTelemetryItem(final String capital, @NonNull final Object value, final FtcLogElement logElement) {
		super(capital, value);
		this.logElement = logElement;
	}

	public FtcLogElement getLogElement() {
		return logElement;
	}
}
