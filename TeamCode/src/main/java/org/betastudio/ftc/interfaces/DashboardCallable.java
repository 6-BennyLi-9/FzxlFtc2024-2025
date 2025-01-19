package org.betastudio.ftc.interfaces;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.message.TelemetryMessage;

public interface DashboardCallable {
	void process(@NonNull TelemetryMessage messageOverride);
}
