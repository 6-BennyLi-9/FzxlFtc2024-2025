package org.betastudio.ftc.ui.client.implementation;

import androidx.annotation.NonNull;

import org.betastudio.ftc.ui.client.ClientEx;
import org.betastudio.ftc.ui.telemetry.TelemetryElement;
import org.betastudio.ftc.ui.telemetry.TelemetryItem;
import org.betastudio.ftc.ui.telemetry.TelemetryLine;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Objects;

public class ExpansionMapClient extends BaseMapClient implements ClientEx {
	public ExpansionMapClient(@NonNull Telemetry telemetry) {
		super(telemetry);
	}

	@Override
	public void putData(String token, String key, String val) {
		data.put(token, new TelemetryItem(key, val));
	}

	@Override
	public void putLine(String token, String val) {
		data.put(token, new TelemetryItem(val, val));
	}

	@Override
	public TelemetryElement getByToken(String token) {
		return data.get(token);
	}

	@Override
	public void deleteByToken(String token) {
		data.remove(token);

		if (autoUpdate){
			update();
		}else {
			isUpdateRequested = false;
		}
	}

	@Override
	public void changeByToken(String token, String val) {
		TelemetryElement element = data.get(token);

		Class <? extends TelemetryElement> clazz = Objects.requireNonNull(element).getClass();
		if (clazz.equals(TelemetryItem.class)) {
			((TelemetryItem) element).setValue(val);
		} else if (clazz.equals(TelemetryLine.class)) {
			((TelemetryLine) element).setLine(val);
		} else {
			throw new IllegalStateException("Unsupported telemetry element type: " + element.getClass().getSimpleName());
		}
	}
}
