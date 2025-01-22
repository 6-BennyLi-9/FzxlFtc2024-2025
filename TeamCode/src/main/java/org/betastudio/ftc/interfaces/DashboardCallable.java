package org.betastudio.ftc.interfaces;

import androidx.annotation.NonNull;

import org.betastudio.ftc.message.TelemetryMessage;

public interface DashboardCallable {
	void process(@NonNull TelemetryMessage messageOverride);
}
