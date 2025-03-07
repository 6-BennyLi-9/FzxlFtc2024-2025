package org.betastudio.ftc.ui.client.implementation;

import androidx.annotation.NonNull;

import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.client.ClientViewMode;
import org.betastudio.ftc.ui.client.UpdateConfig;
import org.betastudio.ftc.ui.log.FtcLogTunnel;
import org.betastudio.ftc.ui.telemetry.TelemetryElement;
import org.betastudio.ftc.ui.telemetry.TelemetryItem;
import org.betastudio.ftc.ui.telemetry.TelemetryLine;
import org.betastudio.ftc.util.message.TelemetryMsg;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ObjectiveClient implements Client {
	protected final Telemetry                    telemetry;
	protected final Map <String, Telemetry.Item> item;
	protected final Map <String, Telemetry.Line> line;
	protected final List <Runnable>              runnables;
	protected       boolean                      autoUpdate;
	protected       boolean                      isUpdateRequested;
	protected       FtcLogTunnel                 targetLogTunnel = FtcLogTunnel.MAIN;

	public ObjectiveClient(Telemetry telemetry) {
		this.telemetry = telemetry;
		item = new HashMap <>();
		line = new HashMap <>();
		runnables = new ArrayList <>();
	}

	@Override
	public void clear() {
		item.values().forEach(telemetry::removeItem);
		item.clear();
		line.values().forEach(telemetry::removeLine);
		line.clear();
		runnables.clear();

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
	}

	@Override
	public void putData(String key, String val) {
		item.put(key, telemetry.addData(key, val));

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
	}

	@Override
	public void deleteData(String key) {
		telemetry.removeItem(item.remove(key));

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
	}

	@Override
	public void changeData(String key, String val) {
		if (item.containsKey(key)) {
			Objects.requireNonNull(item.get(key)).setValue(val);

			if (autoUpdate) {
				this.update();
			} else {
				isUpdateRequested = false;
			}
		} else {
			putData(key, val);
		}
	}

	@Override
	public void putLine(String key) {
		line.put(key, telemetry.addLine(key));

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
	}

	@Override
	public void deleteLine(String key) {
		telemetry.removeLine(line.remove(key));

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
	}

	@Deprecated
	@Override
	public void changeLine(String oldData, String newData) {
		deleteLine(oldData);
		putLine(newData);
	}

	@Override
	public void speak(String text, String languageCode, String countryCode) {
		telemetry.speak(text, languageCode, countryCode);

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
	}

	@Override
	public UpdateConfig getUpdateConfig() {
		return autoUpdate ? UpdateConfig.AUTO_UPDATE_WHEN_OPTION_PUSHED : UpdateConfig.MANUAL_UPDATE_REQUESTED;
	}

	@Override
	public void setUpdateConfig(@NonNull UpdateConfig updateConfig) {
		switch (updateConfig) {
			case AUTO_UPDATE_WHEN_OPTION_PUSHED:
				autoUpdate = true;
				break;
			case MANUAL_UPDATE_REQUESTED:
				autoUpdate = false;
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + updateConfig);
		}
	}

	@Override
	public Telemetry getOriginTelemetry() {
		return telemetry;
	}

	@Override
	public FtcLogTunnel getTargetLogTunnel() {
		return targetLogTunnel;
	}

	@Override
	public void sendMsg(@NonNull TelemetryMsg message) {
		for (final TelemetryElement element : message.getElements()) {
			if (element instanceof TelemetryItem) {
				changeData(((TelemetryItem) element).getCapital(), ((TelemetryItem) element).getValue());
			} else if (element instanceof TelemetryLine) {
				putLine(((TelemetryLine) element).getLine());
			}
		}
	}

	@Override
	public void update() {
		runnables.forEach(Runnable::run);

		switch (ClientViewMode.globalViewMode) {
			case ORIGIN_TELEMETRY:
				telemetry.update();
				break;
			case THREAD_SERVICE:
			case FTC_LOG:
			default:
				throw new UnsupportedOperationException("Unsupported view mode: " + ClientViewMode.globalViewMode);
		}
	}
}
