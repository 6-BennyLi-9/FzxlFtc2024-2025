package org.betastudio.ftc.client;

import android.util.Pair;

import com.acmerobotics.dashboard.config.Config;

import org.betastudio.ftc.client.dashboard.DashTelemetry;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.Timer;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

/**
 * @noinspection UnusedReturnValue
 */
@Config
public class TelemetryClient implements Client {
	public static  ViewMode viewMode;
	public static  boolean  sortDataInTelemetryClientUpdate = true;
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

	public TelemetryClient(final Telemetry telemetry) {
		this.telemetry = telemetry;
		this.data = new HashMap <>();
		lstUpdateTimer.restart();
	}

	@Override
	public void clear() {
		this.data.clear();
		isUpdateRequested=true;
		if (autoUpdate) {
			this.update();
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
		}

		isUpdateRequested=true;
		return this;
	}

	/**
	 * 注意：这是新的Data
	 */
	@Override
	public Client addData(final String key, final Object val) {
		return addData(key, String.valueOf(val));
	}

	/**
	 * @throws RuntimeException 如果未能找到key所指向的值，将会抛出异常
	 */
	@Override
	public Client deleteData(final String key) {
		this.data.remove(key);
		if (autoUpdate) {
			this.update();
		}

		isUpdateRequested=true;
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
		}

		isUpdateRequested=true;
		return this;
	}

	/**
	 * 自动创建新的行如果key所指向的值不存在
	 */
	@Override
	public Client changeData(final String key, final Object val) {
		return changeData(key, String.valueOf(val));
	}

	@Override
	public Client addLine(final String key) {
		++ this.ID;
		this.data.put(key, new Pair <>("", this.ID));
		if (autoUpdate) {
			this.update();
		}

		isUpdateRequested=true;
		return this;
	}

	@Override
	public Client addLine(final Object key) {
		return addLine(String.valueOf(key));
	}

	/**
	 * @throws RuntimeException 如果未能找到key所指向的值，将会抛出异常
	 */
	@Override
	public Client deleteLine(final String key) {
		this.data.remove(key);

		if (autoUpdate) {
			this.update();
		}

		isUpdateRequested=true;
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
		}

		isUpdateRequested=true;
		return this;
	}

	@Override
	public Client speak(String text) {
		try {
			telemetry.speak(text);
		} catch (UnsupportedOperationException ignored) {
		}
		return this;
	}

	@Override
	public Client speak(String text, String languageCode, String countryCode) {
		try {
			telemetry.speak(text, languageCode, countryCode);
		} catch (UnsupportedOperationException ignored) {
		}
		return this;
	}

	@Override
	public void configViewMode(ViewMode viewMode) {
		TelemetryClient.viewMode = viewMode;
	}

	@Override
	public void setAutoUpdate(boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
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
			case BASIC_TELEMETRY:
				updateTelemetryLines();
				break;
			case THREAD_MANAGER:
				updateThreadLines();
				break;
			case LOG:
				throw new UnsupportedOperationException("TelemetryClient doesn't support log view now!");
		}
	}

	@Override
	public boolean isUpdateRequested() {
		return isUpdateRequested;
	}

	protected void updateThreadLines() {
		for (Map.Entry <String, Thread> entry : Global.threadManager.getMem().entrySet()) {
			String key   = entry.getKey();
			Thread value = entry.getValue();
			telemetry.addData(key, value);
		}
		this.telemetry.update();
	}

	protected void updateTelemetryLines() {
		if (sortDataInTelemetryClientUpdate) {
			final Vector <Pair <Integer, Pair <String, String>>> outputData = new Vector <>();
			for (final Map.Entry <String, Pair <String, Integer>> i : this.data.entrySet()) {
				final String  key = i.getKey();
				final String  val = i.getValue().first;
				final Integer id  = i.getValue().second;
				if (! Objects.equals(i.getValue().first, "")) {//line
					outputData.add(new Pair <>(id, new Pair <>(key, val)));
				} else {//line
					outputData.add(new Pair <>(id, new Pair <>(key, null)));
				}
			}
			outputData.sort(Comparator.comparingInt(x -> x.first));

			for (int i = 0 ; i < outputData.size() ; i++) {
				Pair <Integer, Pair <String, String>> outputLine = outputData.get(i);
				if (debug_mode) {
					String packedID = "[" + outputLine.first + "]";
					if (telemetry instanceof DashTelemetry) {
						((DashTelemetry) telemetry).addSmartLine(packedID + outputLine.second.first, outputLine.second.second);
					} else {
						if (null == outputLine.second.second) {
							telemetry.addLine(packedID + outputLine.second.first);
						} else {
							telemetry.addData(packedID + outputLine.second.first, outputLine.second.second);
						}
					}
				} else {
					if (telemetry instanceof DashTelemetry) {
						((DashTelemetry) telemetry).addSmartLine(outputLine.second.first, outputLine.second.second);
					} else {
						if (null == outputLine.second.second) {
							telemetry.addLine(outputLine.second.first);
						} else {
							telemetry.addData(outputLine.second.first, outputLine.second.second);
						}
					}
				}
			}

			this.telemetry.update();
		} else {
			String cache;
			for (final Map.Entry <String, Pair <String, Integer>> entry : this.data.entrySet()) {
				final String                 key = entry.getKey();
				final Pair <String, Integer> val = entry.getValue();
				cache = Objects.equals(val.first, "") ? val.first : key + ":" + val.first;
				if (debug_mode) {
					this.telemetry.addLine("[" + val.second + "]" + cache);
				} else {
					this.telemetry.addLine(key + ":" + cache);
				}
			}
			this.telemetry.update();
		}
	}
}
