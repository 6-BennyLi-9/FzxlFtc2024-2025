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

import java.util.LinkedHashMap;
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

	protected final Map <String, String>                 data;
	protected       boolean                              autoUpdate, isUpdateRequested;
	protected       FtcLogTunnel                         targetLogTunnel = FtcLogTunnel.MAIN;
	protected final Telemetry                            telemetry;

	public BaseMapClient(final Telemetry telemetry) {
		this.telemetry = telemetry;
		this.data = new LinkedHashMap <>();
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

	/**
	 * @param action    the action to execute before composing the lines telemetry
	 */
	@Override
	public Object addAction(final Runnable action) {
		// FIXME: 2025/2/2 这里应该返回一个token，用来在removeAction时删除对应的action
		return null;
	}

	/**
	 * @param token the token previously returned from {@link #addAction(Runnable) addAction()}.
	 */
	@Override
	public boolean removeAction(final Object token) {
		// FIXME: 2025/2/2 这里应该根据token删除对应的action
		return false;
	}

	/**
	 * 注意：这是新的Data
	 */
	@Override
	public Client putData(final String key, final String val) {
		this.data.put(key, val);

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
		return this;
	}

	/**
	 * @throws RuntimeException 如果未能找到key所指向的值，将会抛出异常
	 */
	@Override
	public Client deleteData(final String key) {
		this.data.remove(key);

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
		return this;
	}

	/**
	 * 自动创建新的行如果key所指向的值不存在
	 */
	@Override
	public Client changeData(final String key, final String val) {
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
		return this;
	}

	@Override
	public Client putLine(final String key) {
		this.data.put(key, "");

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
		return this;
	}

	/**
	 * @throws RuntimeException 如果未能找到key所指向的值，将会抛出异常
	 */
	@Override
	public Client deleteLine(final String key) {
		this.data.remove(key);


		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
		return this;
	}

	/**
	 * 将key行替代为val，自动创建新的行如果key所指向的值不存在
	 *
	 * @param oldData 目标行
	 * @param newData 替换行
	 */
	@Override
	public Client changeLine(final String oldData, final String newData) {
		this.data.remove(oldData);
		this.data.put(newData, "");

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
		return this;
	}

	@Override
	public void speak(final String text) {
		try {
			telemetry.speak(text);
		} catch (final UnsupportedOperationException exception) {
			FtcLogTunnel.MAIN.report(exception);
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
	public boolean update() {
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
		final boolean old_isUpdateRequested = isUpdateRequested;
		isUpdateRequested=false;
		return old_isUpdateRequested;
	}

	protected synchronized void updateThreadLines() {
		for (final Map.Entry <String, Thread> entry : Global.threadManager.getMem().entrySet()) {
			final String key   = entry.getKey();
			final Thread value = entry.getValue();
			telemetry.addData(key, value);
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
			case THREAD_REQUIRED:
				throw new IllegalStateException("Cannot set update config to THREAD_REQUIRED for BaseMapClient");
		}
	}

	public FtcLogTunnel getTargetLogTunnel() {
		return targetLogTunnel;
	}

	public void setTargetLogTunnel(final FtcLogTunnel targetLogTunnel) {
		this.targetLogTunnel = targetLogTunnel;
	}
}
