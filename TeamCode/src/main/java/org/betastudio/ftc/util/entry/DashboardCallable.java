package org.betastudio.ftc.util.entry;

import androidx.annotation.NonNull;

import org.betastudio.ftc.util.message.TelemetryMsg;

public interface DashboardCallable {
	void process(@NonNull TelemetryMsg messageOverride);
}
