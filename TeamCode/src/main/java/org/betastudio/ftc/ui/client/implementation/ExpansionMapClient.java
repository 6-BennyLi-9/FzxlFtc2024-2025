package org.betastudio.ftc.ui.client.implementation;

import androidx.annotation.NonNull;

import org.betastudio.ftc.ui.client.ClientEx;
import org.betastudio.ftc.ui.telemetry.TelemetryElement;
import org.betastudio.ftc.ui.telemetry.TelemetryItem;
import org.betastudio.ftc.ui.telemetry.TelemetryLine;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ExpansionMapClient extends BaseMapClient implements ClientEx {
	public ExpansionMapClient(@NonNull final Telemetry telemetry) {
		super(telemetry);
	}

	@Override
	public void putData(final String token, final String key, final String val) {
		data.put(token, new TelemetryItem(key, val));
	}

	@Override
	public void putLine(final String token, final String val) {
		data.put(token, new TelemetryItem(val, val));
	}

	@Override
	public TelemetryElement getByToken(final String token) {
		return data.get(token);
	}

	@Override
	public void deleteByToken(final String token) {
		data.remove(token);

		if (autoUpdate){
			update();
		}else {
			isUpdateRequested = false;
		}
	}

	@Override
	public void changeByToken(final String token, final String val) {
		final TelemetryElement element = data.get(token);

		//noinspection ChainOfInstanceofChecks
		if (element instanceof TelemetryItem) {
			((TelemetryItem) element).setValue(val);
		} else if (element instanceof TelemetryLine) {
			((TelemetryLine) element).setLine(val);
		} else {
			assert null != element;
			throw new IllegalStateException("Unsupported telemetry element type: " + element.getClass().getSimpleName());
		}
	}
}
