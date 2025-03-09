package org.betastudio.ftc.ui.client.implementation;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;

import org.betastudio.ftc.thread.TaskMng;
import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.client.ClientViewMode;
import org.betastudio.ftc.ui.client.UpdateConfig;
import org.betastudio.ftc.ui.log.FtcLogTunnel;
import org.betastudio.ftc.ui.telemetry.TelemetryElement;
import org.betastudio.ftc.ui.telemetry.TelemetryItem;
import org.betastudio.ftc.ui.telemetry.TelemetryLine;
import org.betastudio.ftc.util.Labeler;
import org.betastudio.ftc.util.message.TelemetryMsg;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Global;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @noinspection UnusedReturnValue
 */
@Config
public class BaseMapClient implements Client {
	protected final Telemetry                      telemetry;
	protected final Map <String, TelemetryElement> data;
	protected final List <Runnable>                runnables;
	protected       boolean                        autoUpdate;
	protected       boolean                        isUpdateRequested;
	protected       FtcLogTunnel                   targetLogTunnel = FtcLogTunnel.MAIN;

	public BaseMapClient(@NonNull final Telemetry telemetry) {
		this.telemetry = telemetry;
		this.data = new LinkedHashMap <>();
		runnables = new ArrayList <>();
		telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);
	}

	@Override
	public void clear() {
		this.data.clear();

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
	}

	public Object addAction(final Runnable action) {
		runnables.add(action);
		return action;
	}

	public boolean removeAction(final Object token) {
		return runnables.remove((Runnable) token);
	}

	@Override
	public void putData(final String key, final String val) {
		this.data.put(key, new TelemetryItem(key, val));

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
	}

		@Override
	public void deleteData(final String key) {
		this.data.remove(key);

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
	}

	@Override
	public void changeData(final String key, final String val) {
		if (this.data.containsKey(key)) {
			((TelemetryItem) (Objects.requireNonNull(this.data.get(key)))).setValue(val);
		} else {
			this.putData(key, val);
		}

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
	}

	@Override
	public void putLine(final String key) {
		this.data.put(key, new TelemetryLine(key));

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
	}

	@Override
	public void deleteLine(final String key) {
		this.data.remove(key);

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
	}

	@Override
	public void changeLine(final String oldData, final String newData) {
		this.data.remove(oldData);
		this.data.put(newData, new TelemetryLine(newData));

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
	}

	@Override
	public void speak(final String text, final String languageCode, final String countryCode) {
		try {
			telemetry.speak(text, languageCode, countryCode);
		} catch (final UnsupportedOperationException exception) {
			FtcLogTunnel.MAIN.report(exception);
		}
	}

	@Override
	public Telemetry getOriginTelemetry() {
		return telemetry;
	}

	@Override
	public void update() {
		runnables.forEach(Runnable::run);
		telemetry.clearAll();
		telemetry.addData("ClientViewMode", ClientViewMode.globalViewMode.name());
		telemetry.addData("Status", Global.runMode);
		telemetry.addLine(">>>>>>>>>>>>>>>>>>>");

		switch (ClientViewMode.globalViewMode) {
			case FTC_LOG:
				updateLogLines();
				break;
			case THREAD_SERVICE:
				updateThreadLines();
				break;
			case ORIGIN_TELEMETRY:
			default:
				updateTelemetryLines();
				break;
		}
		isUpdateRequested = false;
	}

	protected synchronized void updateThreadLines() {
		for (final TaskMng.TaskFuture task : Global.service.getTasks()) {
			this.telemetry.addData(task.get(), task.value().isDone() ? "Done" : "Running");
		}
		this.telemetry.update();
	}

	protected synchronized void updateTelemetryLines() {
		for (final Map.Entry <String, TelemetryElement> i : this.data.entrySet()) {
			i.getValue().activateToTelemetry(telemetry);
		}

		this.telemetry.update();
	}

	protected synchronized void updateLogLines() {
		for (final TelemetryElement e : targetLogTunnel.call().getElements()) {
			e.activateToTelemetry(telemetry);
		}
		telemetry.update();
	}

	@Override
	public void sendMsg(@NonNull final TelemetryMsg message) {
		for (final TelemetryElement element : message.getElements()) {
			data.put(Labeler.gen().summon(element), element);
		}
	}

	@Override
	public UpdateConfig getUpdateConfig() {
		return autoUpdate ? UpdateConfig.AUTO_UPDATE_WHEN_OPTION_PUSHED : UpdateConfig.MANUAL_UPDATE_REQUESTED;
	}

	@Override
	public void setUpdateConfig(@NonNull final UpdateConfig updateConfig) {
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
	public FtcLogTunnel getTargetLogTunnel() {
		return targetLogTunnel;
	}

	public void setTargetLogTunnel(final FtcLogTunnel targetLogTunnel) {
		this.targetLogTunnel = targetLogTunnel;
	}
}
