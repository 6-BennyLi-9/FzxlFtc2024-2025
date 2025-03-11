package org.betastudio.ftc.ui.client.implementation;

import androidx.annotation.NonNull;

import org.betastudio.ftc.ui.client.TokenizedClient;
import org.betastudio.ftc.ui.telemetry.TelemetryElement;
import org.betastudio.ftc.ui.telemetry.TelemetryItem;
import org.betastudio.ftc.ui.telemetry.TelemetryLine;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MapTokenizedClient extends BaseMapClient implements TokenizedClient {
	public MapTokenizedClient(@NonNull final Telemetry telemetry) {
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

		//noinspection ChainOfInstanceofChecks
		if (element instanceof TelemetryItem) {
			((TelemetryItem) element).setValue(val);
		} else if (element instanceof TelemetryLine) {
			((TelemetryLine) element).setLine(val);
		} else {
			assert element != null;
			throw new IllegalStateException("Unsupported telemetry element type: " + element.getClass().getSimpleName());
		}
	}
}
