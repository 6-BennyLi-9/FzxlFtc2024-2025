package org.betastudio.ftc.telemetry;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TelemetryItem implements TelemetryElement{
	public final String capital;
	public String value;

	public TelemetryItem(String capital, String value) {
		this.capital = capital;
		this.value = value;
	}

	@Override
	public void push(@NonNull Telemetry telemetry) {
		telemetry.addData(capital, value);
	}
}
