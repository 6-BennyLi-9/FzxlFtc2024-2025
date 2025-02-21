package org.betastudio.ftc.ui.telemetry;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TelemetryLine implements TelemetryElement {
	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String line;

	public TelemetryLine(final String line) {
		this.line = line;
	}

	@Override
	public void activateToTelemetry(@NonNull final Telemetry telemetry) {
		telemetry.addLine(line);
	}

	@NonNull
	@Override
	public String toString() {
		return line;
	}
}
