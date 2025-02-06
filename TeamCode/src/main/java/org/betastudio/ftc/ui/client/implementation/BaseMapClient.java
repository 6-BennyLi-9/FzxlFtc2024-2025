package org.betastudio.ftc.ui.client.implementation;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;

import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.ui.client.ClientViewMode;
import org.betastudio.ftc.ui.client.UpdateConfig;
import org.betastudio.ftc.ui.log.FtcLogTunnel;
import org.betastudio.ftc.ui.telemetry.TelemetryElement;
import org.betastudio.ftc.ui.telemetry.TelemetryItem;
import org.betastudio.ftc.ui.telemetry.TelemetryLine;
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
	public static ClientViewMode clientViewMode;
	public static boolean        debug_mode;

	static {
		clientViewMode = ClientViewMode.ORIGIN_TELEMETRY;
	}

	protected final Telemetry            telemetry;
	protected final Map <String, String> data;
	protected final List <Runnable>      runnables;
	protected       boolean              autoUpdate, isUpdateRequested;
	protected       FtcLogTunnel         targetLogTunnel = FtcLogTunnel.MAIN;

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

	/**
	 * 注意：这是新的Data
	 */
	@Override
	public void putData(final String key, final String val) {
		this.data.put(key, val);

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
	}

	/**
	 * @throws RuntimeException 如果未能找到key所指向的值，将会抛出异常
	 */
	@Override
	public void deleteData(final String key) {
		this.data.remove(key);

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
	}

	/**
	 * 自动创建新的行如果key所指向的值不存在
	 */
	@Override
	public void changeData(final String key, final String val) {
		if (this.data.containsKey(key)) {
			this.data.replace(key, val);
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
		this.data.put(key, "");

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
	}

	/**
	 * @throws RuntimeException 如果未能找到key所指向的值，将会抛出异常
	 */
	@Override
	public void deleteLine(final String key) {
		this.data.remove(key);


		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
	}

	/**
	 * 将key行替代为val，自动创建新的行如果key所指向的值不存在
	 *
	 * @param oldData 目标行
	 * @param newData 替换行
	 */
	@Override
	public void changeLine(final String oldData, final String newData) {
		this.data.remove(oldData);
		this.data.put(newData, "");

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
	public void configViewMode(final ClientViewMode clientViewMode) {
		BaseMapClient.clientViewMode = clientViewMode;
	}

	@Override
	public ClientViewMode getCurrentViewMode() {
		return clientViewMode;
	}

	@Override
	public Telemetry getOriginTelemetry() {
		return telemetry;
	}

	@Override
	public void update() {
		telemetry.clearAll();
		telemetry.addData("ClientViewMode", clientViewMode.name());
		telemetry.addData("Status", Global.runMode);
		telemetry.addLine(">>>>>>>>>>>>>>>>>>>");

		switch (clientViewMode) {
			case FTC_LOG:
				updateLogLines();
				break;
			case THREAD_MANAGER:
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
		for (final Thread thread : Global.threadService.getThreads()) {
			telemetry.addData(thread.getName(), thread.getState().name());
		}
		this.telemetry.update();
	}

	protected synchronized void updateTelemetryLines() {
		for (final Map.Entry <String, String> i : this.data.entrySet()) {
			final String  key = i.getKey();
			final String  val = i.getValue();
			if (! Objects.equals(val, "")) {
				telemetry.addData(key, val);
			} else {
				telemetry.addLine(key);
			}
		}

		this.telemetry.update();
	}

	protected synchronized void updateLogLines() {
		for (final TelemetryElement e : targetLogTunnel.call().getElements()) {
			e.push(telemetry);
		}
		telemetry.update();
	}

	@Override
	public void sendMsg(@NonNull final TelemetryMsg message) {
		for (final TelemetryElement element : message.getElements()) {
			if (element instanceof TelemetryLine) {
				putLine(((TelemetryLine) element).line);
			} else if (element instanceof TelemetryItem) {
				changeData(((TelemetryItem) element).capital, ((TelemetryItem) element).value);
			}
		}
	}

	@Override
	public boolean isUpdateRequested() {
		return isUpdateRequested;
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
		}
	}

	public FtcLogTunnel getTargetLogTunnel() {
		return targetLogTunnel;
	}

	public void setTargetLogTunnel(final FtcLogTunnel targetLogTunnel) {
		this.targetLogTunnel = targetLogTunnel;
	}
}
