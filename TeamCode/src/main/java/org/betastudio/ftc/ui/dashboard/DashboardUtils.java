package org.betastudio.ftc.ui.dashboard;

import static org.betastudio.ftc.Interfaces.Updatable;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.jetbrains.annotations.Contract;

import java.util.HashSet;
import java.util.Set;

@Config
public class DashboardUtils implements Updatable{
	private static final Set <TelemetryPacket> telemetryPackets = new HashSet <>();
	public static        boolean               updateRequested  = true;
	private static       FtcDashboard          dashboard;
	private static       TelemetryPacket       currentPacket    = new TelemetryPacket();

	public static void fetch() {
		dashboard = FtcDashboard.getInstance();
	}

	public static void sendTelemetryPacket(final TelemetryPacket packet) {
		telemetryPackets.add(packet);
		updateRequested = true;
	}

	@NonNull
	@Contract(" -> new")
	public static DashboardUtils generateInstance() {
		return new DashboardUtils();
	}

	public static Canvas getCanvas() {
		return currentPacket.fieldOverlay();
	}

	@Override
	public void update() {
		updateRequested = false;
		telemetryPackets.add(currentPacket);
		for (final TelemetryPacket packet : telemetryPackets) {
			dashboard.sendTelemetryPacket(packet);
		}
		telemetryPackets.clear();
		dashboard.updateConfig();
		currentPacket = new TelemetryPacket();
	}

	public TelemetryPacket getCurrentPacket() {
		return currentPacket;
	}
}
