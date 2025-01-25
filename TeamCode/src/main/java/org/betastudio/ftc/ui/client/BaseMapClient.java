package org.betastudio.ftc.ui.client;

import android.util.Pair;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;

import org.betastudio.ftc.ui.log.FtcLogTunnel;
import org.betastudio.ftc.util.message.TelemetryMessage;
import org.betastudio.ftc.ui.telemetry.TelemetryElement;
import org.betastudio.ftc.ui.telemetry.TelemetryItem;
import org.betastudio.ftc.ui.telemetry.TelemetryLine;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Global;
import org.betastudio.ftc.util.Timer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @noinspection UnusedReturnValue
 */
@Config
public class BaseMapClient implements Client {
	public static  ViewMode viewMode;
	public static  boolean  debug_mode;
	private boolean isUpdateRequested;

	static {
		viewMode = ViewMode.BASIC_TELEMETRY;
	}

	protected final Map <String, Pair <String, Integer>> data;
	private final   Telemetry                            telemetry;
	private final   Timer                                lstUpdateTimer = new Timer();
	protected       int                                  ID;
	private         boolean                              autoUpdate;
	private         FtcLogTunnel                         targetLogTunnel;

	public BaseMapClient(final Telemetry telemetry) {
		this.telemetry = telemetry;
		this.data = new LinkedHashMap <>();
		lstUpdateTimer.restart();
	}

	@Override
	public void clear() {
		this.data.clear();

		if (autoUpdate) {
			this.update();
		}else{
			isUpdateRequested=false;
		}
	}

	/**
	 * 注意：这是新的Data
	 */
	@Override
	public Client addData(final String key, final String val) {
		++ this.ID;
		this.data.put(key, new Pair <>(val, this.ID));

		if (autoUpdate) {
			this.update();
		}else{
			isUpdateRequested=false;
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
		}else{
			isUpdateRequested=false;
		}
		return this;
	}

	/**
	 * 自动创建新的行如果key所指向的值不存在
	 */
	@Override
	public Client changeData(final String key, final String val) {
		if (this.data.containsKey(key)) {
			this.data.replace(key, new Pair <>(val, (Objects.requireNonNull(this.data.get(key))).second));
		} else {
			this.addData(key, val);
		}

		if (autoUpdate) {
			this.update();
		}else{
			isUpdateRequested=false;
		}
		return this;
	}

	@Override
	public Client addLine(final String key) {
		++ this.ID;
		this.data.put(key, new Pair <>("", this.ID));

		if (autoUpdate) {
			this.update();
		}else{
			isUpdateRequested=false;
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
		}else{
			isUpdateRequested=false;
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
		final int cache = Objects.requireNonNull(this.data.get(oldData)).second;
		this.data.remove(oldData);
		this.data.put(newData, new Pair <>("", cache));

		if (autoUpdate) {
			this.update();
		}else{
			isUpdateRequested=false;
		}
		return this;
	}

	@Override
	public Client speak(final String text) {
		try {
			telemetry.speak(text);
		} catch (final UnsupportedOperationException ignored) {
		}
		return this;
	}

	@Override
	public Client speak(final String text, final String languageCode, final String countryCode) {
		try {
			telemetry.speak(text, languageCode, countryCode);
		} catch (final UnsupportedOperationException ignored) {
		}
		return this;
	}

	@Override
	public void configViewMode(final ViewMode viewMode) {
		BaseMapClient.viewMode = viewMode;
	}

	@Override
	public ViewMode getCurrentViewMode() {
		return viewMode;
	}

	@Override
	public void update() {
		telemetry.clearAll();
		if (debug_mode) {
			telemetry.addData("Update Delta Time", lstUpdateTimer.restartAndGetDeltaTime());
		}
		telemetry.addData("ViewMode", viewMode.name());
		telemetry.addData("Status", Global.runMode);
		telemetry.addLine(">>>>>>>>>>>>>>>>>>>");

		switch (viewMode) {
			case LOG:
				updateLogLines(targetLogTunnel);
				break;
			case THREAD_MANAGER:
				updateThreadLines();
				break;
			case BASIC_TELEMETRY:
			default:
				updateTelemetryLines();
				break;
		}
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
		for (final Map.Entry <String, Pair <String, Integer>> i : this.data.entrySet()) {
			final String  key = i.getKey();
			final String  val = i.getValue().first;
			final Integer id  = i.getValue().second;
			if (! Objects.equals(i.getValue().first, "")) {
				telemetry.addData(key, val);
			} else {//line
				telemetry.addLine(key);
			}
		}

		this.telemetry.update();
	}

	protected synchronized void updateLogLines(@NonNull final FtcLogTunnel type) {
		send(type.call());

		this.telemetry.update();
	}

	@Override
	public void send(@NonNull final TelemetryMessage message) {
		for (final TelemetryElement element : message.elements) {
			if (element instanceof TelemetryLine) {
				addLine(((TelemetryLine) element).line);
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
	public void setUpdateConfig(@NonNull final UpdateConfig updateConfig) {
		switch (updateConfig) {
			case AUTO_UPDATE_WHEN_OPTION_PUSHED:
				autoUpdate=true;
				break;
			case MANUAL_UPDATE_REQUESTED:
				autoUpdate=false;
				break;
			case THREAD_REQUIRED:
				throw new IllegalStateException("Cannot set update config to THREAD_REQUIRED for BaseMapClient");
		}
	}

	@Override
	public UpdateConfig getUpdateConfig() {
		return autoUpdate ? UpdateConfig.AUTO_UPDATE_WHEN_OPTION_PUSHED : UpdateConfig.MANUAL_UPDATE_REQUESTED;
	}
}
