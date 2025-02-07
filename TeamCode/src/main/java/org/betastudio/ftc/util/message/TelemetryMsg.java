package org.betastudio.ftc.util.message;

import androidx.annotation.NonNull;

import org.betastudio.ftc.ui.telemetry.TelemetryElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TelemetryMsg implements Message {
	private final List <TelemetryElement> elements;

	public TelemetryMsg(@NonNull final List <TelemetryElement> elements) {
		this.elements = elements;
	}

	public TelemetryMsg(final TelemetryElement... elements) {
		this(new ArrayList <>(Arrays.asList(elements)));
	}

	public void add(final TelemetryElement element) {
		elements.add(element);
	}

	public void addAll(final Collection <TelemetryElement> elements) {
		this.elements.addAll(elements);
	}

	public List <TelemetryElement> getElements() {
		return elements;
	}

	public void merge(@NonNull final TelemetryMsg message) {
		elements.addAll(message.elements);
	}
}
