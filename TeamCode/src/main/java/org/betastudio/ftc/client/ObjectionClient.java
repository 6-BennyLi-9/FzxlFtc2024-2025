package org.betastudio.ftc.client;

import androidx.annotation.NonNull;

import org.betastudio.ftc.telemetry.TelemetryElement;
import org.betastudio.ftc.telemetry.TelemetryItem;
import org.betastudio.ftc.telemetry.TelemetryLine;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.Telemetry.Item;
import org.firstinspires.ftc.robotcore.external.Telemetry.Line;
import org.betastudio.ftc.message.TelemetryMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Deprecated
public class ObjectionClient implements Client {
	protected final Map <String, Item> data;
	protected final Map <String, Line> line;
	private final   Telemetry          telemetry;
	private boolean autoUpdate = true;
	private boolean isUpdateRequested;

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

	@Override
	public Client addData(final String key, final String val) {
		data.put(key, telemetry.addData(key, val));

		if (autoUpdate) {
			this.update();
		}else{
			isUpdateRequested=false;
		}
		return this;
	}

	@Override
	public Client addData(final String key, @NonNull final Object val) {
		return addData(key, val.toString());
	}

	@Override
	public Client deleteData(final String key) {
		telemetry.removeItem(data.get(key));

		if (autoUpdate) {
			this.update();
		}else{
			isUpdateRequested=false;
		}
		return this;
	}

	@Override
	public Client changeData(final String key, final String val) {
		if (data.containsKey(key)){
			Objects.requireNonNull(data.get(key)).setValue(val);
		}else{
			return addData(key, val);
		}

		if (autoUpdate) {
			this.update();
		}else{
			isUpdateRequested=false;
		}
		return this;
	}

	@Override
	public Client changeData(final String key, @NonNull final Object val) {
		return changeData(key, val.toString());
	}

	@Override
	public Client addLine(final String key) {
		line.put(key, telemetry.addLine(key));

		if (autoUpdate) {
			this.update();
		}else{
			isUpdateRequested=false;
		}
		return this;
	}

	@Override
	public Client addLine(@NonNull final Object key) {
		return addLine(key.toString());
	}

	@Override
	public Client deleteLine(final String key) {
		telemetry.removeLine(line.get(key));

		if (autoUpdate) {
			this.update();
		}else{
			isUpdateRequested=false;
		}
		return this;
	}

	@Override
	public Client speak(final String text) {
		return speak(text, null,null);
	}

	@Override
	public Client speak(final String text, final String languageCode, final String countryCode) {
		telemetry.speak(text,languageCode,countryCode);
		if (autoUpdate){
			update();
		}
		return this;
	}

	@Override
	public void setAutoUpdate(final boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
	}

	@Override
	public void update() {
		telemetry.update();
	}

	@Override
	public boolean isUpdateRequested() {
		return isUpdateRequested;
	}

	@Override
	public void send(@NonNull final TelemetryMessage message) {
		for (final TelemetryElement element : message.elements) {
			if (element instanceof TelemetryLine) {
				addLine(((TelemetryLine) element).line);
			} else if (element instanceof TelemetryItem) {
				addData(((TelemetryItem) element).capital, ((TelemetryItem) element).value);
			}
		}
	}

//  -----------------------
//  UNSUPPORTED METHODS
//  -----------------------

	@Override
	public ViewMode getCurrentViewMode() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void configViewMode(final ViewMode viewMode) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Client changeLine(final String oldData, final String newData) {
		throw new UnsupportedOperationException();
	}
}
