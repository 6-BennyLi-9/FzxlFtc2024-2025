package org.betastudio.ftc.interfaces;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

public interface DashboardCallable {
	void sendToDashboard(@NonNull TelemetryPacket packet);
}
