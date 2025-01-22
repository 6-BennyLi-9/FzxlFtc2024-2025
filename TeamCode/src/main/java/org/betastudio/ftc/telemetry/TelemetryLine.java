package org.betastudio.ftc.telemetry;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TelemetryLine implements TelemetryElement{
	public final String line;

	public TelemetryLine(final String line) {
		this.line = line;
	}

	@Override
	public void push(@NonNull final Telemetry telemetry) {
		telemetry.addLine(line);
	}
}
