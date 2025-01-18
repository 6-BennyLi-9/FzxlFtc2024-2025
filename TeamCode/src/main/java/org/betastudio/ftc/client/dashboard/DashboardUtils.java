package org.betastudio.ftc.client.dashboard;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.betastudio.ftc.interfaces.Updatable;
import org.jetbrains.annotations.Contract;

import java.util.Set;

public class DashboardUtils implements Updatable {
	private static FtcDashboard dashboard;
	private static Set<TelemetryPacket> telemetryPackets;
	private static boolean updateRequested;

	public static void fetch(){
		dashboard=FtcDashboard.getInstance();
	}

	public static void sendTelemetryPacket(TelemetryPacket packet){
		telemetryPackets.add(packet);
		updateRequested=true;
	}

	@NonNull
	@Contract(" -> new")
	public static DashboardUtils generateInstance(){
		return new DashboardUtils();
	}

	@Override
	public void update() {
		updateRequested=false;
		if (!telemetryPackets.isEmpty()){
			for (TelemetryPacket packet : telemetryPackets) {
				dashboard.sendTelemetryPacket(packet);
			}
		}
		telemetryPackets.clear();
		dashboard.updateConfig();
	}

	@Override
	public boolean isUpdateRequested() {
		return updateRequested;
	}
}
