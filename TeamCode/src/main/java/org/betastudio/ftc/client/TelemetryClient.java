package org.betastudio.ftc.client;

import android.util.Pair;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

/**
 * @noinspection UnusedReturnValue
 */
@Config
public class TelemetryClient {
	private final   Telemetry                            telemetry;
	protected final Map <String, Pair <String, Integer>> data;
	protected       int                                  ID;
	public static   boolean                              showIndex;
	/**
	 * 若启用，更改数据后需要执行 {@link #update()} ，关闭时则无需执行
	 */
	public          boolean                              autoUpdate;
	private static  TelemetryClient                      instanceTelemetryClient;

	public TelemetryClient(final Telemetry telemetry) {
		this.telemetry = telemetry;
		this.data = new HashMap <>();
		instanceTelemetryClient = this;
	}

	public static TelemetryClient getInstance() {
		return instanceTelemetryClient;
	}

	public static void constructInstance(final Telemetry telemetry) {
		instanceTelemetryClient = new TelemetryClient(telemetry);
	}

	public void clear() {
		this.data.clear();
		if (! autoUpdate) {
			this.update();
		}
	}

	/**
	 * 注意：这是新的Data
	 */
	public TelemetryClient addData(final String key, final String val) {
		++ this.ID;
		this.data.put(key, new Pair <>(val, this.ID));
		if (! autoUpdate) {
			this.update();
		}

		return this;
	}

	/**
	 * 注意：这是新的Data
	 */
	public TelemetryClient addData(final String key, final Object val) {
		return addData(key, String.valueOf(val));
	}

	/**
	 * @throws RuntimeException 如果未能找到key所指向的值，将会抛出异常
	 */
	public TelemetryClient deleteData(final String key) {
		this.data.remove(key);
		if (! autoUpdate) {
			this.update();
		}

		return this;
	}

	/**
	 * 自动创建新的行如果key所指向的值不存在
	 */
	public TelemetryClient changeData(final String key, final String val) {
		if (this.data.containsKey(key)) {
			this.data.replace(key, new Pair <>(val, (Objects.requireNonNull(this.data.get(key))).second));
		} else {
			this.addData(key, val);
		}
		if (! autoUpdate) {
			this.update();
		}

		return this;
	}

	/**
	 * 自动创建新的行如果key所指向的值不存在
	 */
	public TelemetryClient changeData(final String key, final Object val) {
		return changeData(key, String.valueOf(val));
	}

	public TelemetryClient addLine(final String key) {
		++ this.ID;
		this.data.put(key, new Pair <>("", this.ID));
		if (! autoUpdate) {
			this.update();
		}

		return this;
	}

	public TelemetryClient addLine(final Object key) {
		return addLine(String.valueOf(key));
	}

	/**
	 * @throws RuntimeException 如果未能找到key所指向的值，将会抛出异常
	 */
	public TelemetryClient deleteLine(final String key) {
		this.data.remove(key);

		if (! autoUpdate) {
			this.update();
		}
		return this;
	}

	/**
	 * 将key行替代为val，自动创建新的行如果key所指向的值不存在
	 *
	 * @param oldData 目标行
	 * @param newData 替换行
	 */
	public TelemetryClient changeLine(final String oldData, final String newData) {
		final int cache = Objects.requireNonNull(this.data.get(oldData)).second;
		this.data.remove(oldData);
		this.data.put(newData, new Pair <>("", cache));
		if (! autoUpdate) {
			this.update();
		}

		return this;
	}

	public static boolean sortDataInTelemetryClientUpdate = true;

	public void update() {
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
				if (showIndex) {
					String packedID = "[" + outputLine.first + "]";
					if (telemetry instanceof DashTelemetry) {
						((DashTelemetry) telemetry).addSmartLine(packedID + outputLine.second.first, outputLine.second.second);
					} else {
						if (outputLine.second.second == null) {
							telemetry.addLine(packedID + outputLine.second.first);
						} else {
							telemetry.addData(packedID + outputLine.second.first, outputLine.second.second);
						}
					}
				} else {
					if (telemetry instanceof DashTelemetry) {
						((DashTelemetry) telemetry).addSmartLine(outputLine.second.first, outputLine.second.second);
					} else {
						if (outputLine.second.second == null) {
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
				if (showIndex) {
					this.telemetry.addLine("[" + val.second + "]" + cache);
				} else {
					this.telemetry.addLine(key + ":" + cache);
				}
			}
			this.telemetry.update();
		}
	}
}
