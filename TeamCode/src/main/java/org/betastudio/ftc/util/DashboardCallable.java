package org.betastudio.ftc.util;

import androidx.annotation.NonNull;

import org.betastudio.ftc.message.TelemetryMessage;

public interface DashboardCallable {
	void process(@NonNull TelemetryMessage messageOverride);
}
