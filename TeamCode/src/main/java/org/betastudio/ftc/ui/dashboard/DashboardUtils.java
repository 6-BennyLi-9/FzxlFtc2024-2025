package org.betastudio.ftc.ui.dashboard;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.betastudio.ftc.specification.MessagesProcessRequired;
import org.betastudio.ftc.specification.Updatable;
import org.betastudio.ftc.ui.telemetry.TelemetryElement;
import org.betastudio.ftc.ui.telemetry.TelemetryItem;
import org.betastudio.ftc.ui.telemetry.TelemetryLine;
import org.betastudio.ftc.util.message.TelemetryMsg;
import org.jetbrains.annotations.Contract;

import java.util.HashSet;
import java.util.Set;

@Config
public class DashboardUtils implements Updatable , MessagesProcessRequired<TelemetryMsg> {
	private static final Set <TelemetryPacket> telemetryPackets = new HashSet <>();
	public static        boolean               recordElements;
	public static        boolean               updateRequested  = true;
	private static       FtcDashboard          dashboard;
	private static       TelemetryPacket       currentPacket    = new TelemetryPacket();
	private static       TelemetryMsg          currentMessage   = new TelemetryMsg(new HashSet<>());

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

	public static void addLine(final String line) {
		if (recordElements) {
			currentMessage.add(new TelemetryLine(line));
		}
		currentPacket.addLine(line);
	}

	public static void put(final String capital, final String value) {
		if (recordElements) {
			currentMessage.add(new TelemetryItem(capital, value));
		}
		currentPacket.put(capital, value);
	}

	public static Canvas getCanvas() {
		return currentPacket.fieldOverlay();
	}

	@Override
	public boolean isUpdateRequested() {
		return updateRequested;
	}

	public TelemetryPacket getCurrentPacket() {
		return currentPacket;
	}

	@Override
	public void send(@NonNull final TelemetryMsg message) {
		final TelemetryPacket packet = new TelemetryPacket();
		for (final TelemetryElement element : message.getElements()) {
			if (element instanceof TelemetryLine) {
				packet.addLine(((TelemetryLine) element).line);
			} else if (element instanceof TelemetryItem) {
				packet.put(((TelemetryItem) element).capital, ((TelemetryItem) element).value);
			}
		}
	}

	@Override
	public TelemetryMsg call() {
		if (! recordElements) {
			throw new IllegalStateException("Telemetry recording is not enabled");
		}
		final TelemetryMsg res =new TelemetryMsg(new HashSet <>(currentMessage.getElements()));
		currentMessage = new TelemetryMsg(new HashSet<>());
		return res;
	}
}
