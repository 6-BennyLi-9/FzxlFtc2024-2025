package org.betastudio.ftc.ui.telemetry;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TelemetryItem implements TelemetryElement {
	public String getCapital() {
		return capital;
	}

	public void setCapital(final String capital) {
		this.capital = capital;
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public String capital, value;

	public TelemetryItem(final String capital, final String value) {
		this.capital = capital;
		this.value = value;
	}

	public TelemetryItem(final String capital, @NonNull final Object value) {
		this(capital, value.toString());
	}

	@Override
	public void activateToTelemetry(@NonNull final Telemetry telemetry) {
		telemetry.addData(capital, value);
	}

	@NonNull
	@Override
	public String toString() {
		return capital + ":" + value;
	}
}
