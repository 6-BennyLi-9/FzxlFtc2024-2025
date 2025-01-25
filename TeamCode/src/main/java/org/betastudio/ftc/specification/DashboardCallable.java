package org.betastudio.ftc.specification;

import androidx.annotation.NonNull;

import org.betastudio.ftc.util.message.TelemetryMessage;

public interface DashboardCallable {
	void process(@NonNull TelemetryMessage messageOverride);
}
