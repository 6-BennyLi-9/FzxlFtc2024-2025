package org.betastudio.ftc.telemetry;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public interface TelemetryElement {
	void push(@NonNull Telemetry telemetry);
}
