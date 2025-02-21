package org.betastudio.ftc.ui.telemetry;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TelemetryItem implements TelemetryElement {
	public String capital, value;

	public TelemetryItem(final String capital, final String value) {
		this.capital = capital;
		this.value = value;
	}

	public TelemetryItem(final String capital, @NonNull final Object value) {
		this(capital, value.toString());
	}

	@Override
	public void push(@NonNull final Telemetry telemetry) {
		telemetry.addData(capital, value);
	}

	@Override
	public String toString() {
		return capital + ":" + value;
	}
}
