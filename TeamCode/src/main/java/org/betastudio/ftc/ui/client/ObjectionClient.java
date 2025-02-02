package org.betastudio.ftc.ui.client;

import androidx.annotation.NonNull;

import org.betastudio.ftc.ui.telemetry.TelemetryElement;
import org.betastudio.ftc.ui.telemetry.TelemetryItem;
import org.betastudio.ftc.ui.telemetry.TelemetryLine;
import org.betastudio.ftc.util.message.TelemetryMsg;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Deprecated
public class ObjectionClient implements Client {
	protected final Map <String, Item> data;
	protected final Map <String, Line> line;
	private final   Telemetry          telemetry;
	private         boolean            autoUpdate = true;
	private         boolean            isUpdateRequested;

	public ObjectionClient(final Telemetry telemetry) {
		this.telemetry = telemetry;
		this.data = new HashMap <>();
		this.line = new HashMap <>();
	}

	@Override
	public void clear() {
		telemetry.clearAll();
		data.clear();
		line.clear();
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

	@Override
	public Client putData(final String key, final String val) {
		data.put(key, telemetry.addData(key, val));

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
		return this;
	}

	@Override
	public Client deleteData(final String key) {
		telemetry.removeItem(data.get(key));

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
		return this;
	}

	@Override
	public Client changeData(final String key, final String val) {
		if (data.containsKey(key)) {
			Objects.requireNonNull(data.get(key)).setValue(val);
		} else {
			return putData(key, val);
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
		line.put(key, telemetry.addLine(key));

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
		return this;
	}

	@Override
	public Client deleteLine(final String key) {
		telemetry.removeLine(line.get(key));

		if (autoUpdate) {
			this.update();
		} else {
			isUpdateRequested = false;
		}
		return this;
	}

	@Override
	public void speak(final String text) {
		speak(text, null, null);
	}

	@Override
	public void speak(final String text, final String languageCode, final String countryCode) {
		telemetry.speak(text, languageCode, countryCode);
		if (autoUpdate) {
			update();
		}
	}

	@Override
	public boolean update() {
		return telemetry.update();
	}

	@Override
	public boolean isUpdateRequested() {
		return isUpdateRequested;
	}

	@Override
	public void sendMsg(@NonNull final TelemetryMsg message) {
		for (final TelemetryElement element : message.getElements()) {
			if (element instanceof TelemetryLine) {
				putLine(((TelemetryLine) element).line);
			} else if (element instanceof TelemetryItem) {
				putData(((TelemetryItem) element).capital, ((TelemetryItem) element).value);
			}
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
			case THREAD_REQUIRED:
				throw new IllegalStateException("Cannot set update config to THREAD_REQUIRED for BaseMapClient");
		}
	}

	//  -----------------------
	//  UNSUPPORTED METHODS
	//  -----------------------

	@Override
	public ClientViewMode getCurrentViewMode() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Telemetry getOriginTelemetry() {
		return telemetry;
	}

	@Override
	public void configViewMode(final ClientViewMode clientViewMode) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Client changeLine(final String oldData, final String newData) {
		throw new UnsupportedOperationException();
	}
}
