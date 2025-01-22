package org.betastudio.ftc.telemetry;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TelemetryItem implements TelemetryElement{
	public final String capital;
	public String value;

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
}
