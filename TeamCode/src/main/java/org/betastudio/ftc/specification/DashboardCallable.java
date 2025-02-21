package org.betastudio.ftc.specification;

import androidx.annotation.NonNull;

import org.betastudio.ftc.util.message.TelemetryMsg;

public interface DashboardCallable {
	void process(@NonNull TelemetryMsg messageOverride);
}
