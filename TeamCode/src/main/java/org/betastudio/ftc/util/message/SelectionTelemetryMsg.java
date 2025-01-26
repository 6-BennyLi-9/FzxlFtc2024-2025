package org.betastudio.ftc.util.message;

import androidx.annotation.NonNull;

import org.betastudio.ftc.ui.telemetry.IntegralSelectElement;
import org.betastudio.ftc.ui.telemetry.SelectionTeleElement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectionTelemetryMsg implements Message {
	protected final List <SelectionTeleElement> elements;

	public SelectionTelemetryMsg(@NonNull final Set <SelectionTeleElement> elements) {
		this.elements = new ArrayList <>(elements);
	}

	public SelectionTelemetryMsg(final SelectionTeleElement... elements) {
		this(Set.of(elements));
	}

	public void add(final SelectionTeleElement element) {
		elements.add(element);
	}

	public void add(@NonNull final SelectionTelemetryMsg message) {
		elements.addAll(message.elements);
	}

	public List <SelectionTeleElement> getElements() {
		return elements;
	}

	public void press(final int index) {
		elements.get(index).press();
	}

	public void submit(final int index) {
		assert elements.get(index) instanceof IntegralSelectElement;
		((IntegralSelectElement) elements.get(index)).submit();
	}

	public TelemetryMsg convertToTelemetryMsg() {
		final TelemetryMsg telemetryMsg = new TelemetryMsg();
		telemetryMsg.addAll(new HashSet <>(elements));
		return telemetryMsg;
	}
}
