package org.betastudio.ftc.ui.telemetry;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public interface TelemetryElement {
	void push(@NonNull Telemetry telemetry);
}
