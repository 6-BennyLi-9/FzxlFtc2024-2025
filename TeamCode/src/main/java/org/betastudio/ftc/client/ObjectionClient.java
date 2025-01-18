package org.betastudio.ftc.client;

import androidx.annotation.NonNull;

import org.betastudio.ftc.telemetry.TelemetryElement;
import org.betastudio.ftc.telemetry.TelemetryItem;
import org.betastudio.ftc.telemetry.TelemetryLine;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.Telemetry.Item;
import org.firstinspires.ftc.robotcore.external.Telemetry.Line;
import org.firstinspires.ftc.teamcode.message.TelemetryMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Deprecated
public class ObjectionClient implements Client {
	protected final Map <String, Item> data;
	protected final Map <String, Line> line;
	private final   Telemetry          telemetry;
	private boolean autoUpdate = true;
	private boolean isUpdateRequested = false;

	public ObjectionClient(Telemetry telemetry) {
		this.telemetry = telemetry;
		this.data = new HashMap <>();
		this.line = new HashMap <>();
	}
	
	@Override
	public void clear() {
		telemetry.clear();
		data.clear();
		line.clear();
	}

	@Override
	public Client addData(String key, String val) {
		data.put(key, telemetry.addData(key, val));
		if (autoUpdate){
			update();
		}

		isUpdateRequested=true;
		return this;
	}

	@Override
	public Client addData(String key, Object val) {
		return addData(key, val.toString());
	}

	@Override
	public Client deleteData(String key) {
		telemetry.removeItem(data.get(key));
		if (autoUpdate){
			update();
		}

		isUpdateRequested=true;
		return this;
	}

	@Override
	public Client changeData(String key, String val) {
		if (data.containsKey(key)){
			Objects.requireNonNull(data.get(key)).setValue(val);
		}else{
			return addData(key, val);
		}
		if (autoUpdate){
			update();
		}

		isUpdateRequested=true;
		return this;
	}

	@Override
	public Client changeData(String key, Object val) {
		return changeData(key, val.toString());
	}

	@Override
	public Client addLine(String key) {
		line.put(key, telemetry.addLine(key));
		if (autoUpdate){
			update();
		}

		isUpdateRequested=true;
		return this;
	}

	@Override
	public Client addLine(Object key) {
		return addLine(key.toString());
	}

	@Override
	public Client deleteLine(String key) {
		telemetry.removeLine(line.get(key));
		if (autoUpdate){
			update();
		}

		isUpdateRequested=true;
		return this;
	}

	@Override
	public Client changeLine(String oldData, String newData) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Client speak(String text) {
		return speak(text, null,null);
	}

	@Override
	public Client speak(String text, String languageCode, String countryCode) {
		telemetry.speak(text,languageCode,countryCode);
		if (autoUpdate){
			update();
		}
		return this;
	}

	@Override
	public void configViewMode(ViewMode viewMode) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setAutoUpdate(boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
	}

	@Override
	public ViewMode getCurrentViewMode() {
		throw new UnsupportedOperationException();
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
	public void sendRequest(@NonNull TelemetryMessage message) {
		for (TelemetryElement element : message.elements) {
			if (element instanceof TelemetryLine) {
				addLine(((TelemetryLine) element).line);
			} else if (element instanceof TelemetryItem) {
				addData(((TelemetryItem) element).capital, ((TelemetryItem) element).value);
			}
		}
	}
}
