package org.betastudio.ftc.util.message;

import androidx.annotation.NonNull;

import org.betastudio.ftc.ui.telemetry.TelemetryElement;

import java.util.Set;

public class TelemetryMessage implements Message{
	private final Set<TelemetryElement> elements;

	public TelemetryMessage(final Set<TelemetryElement> elements) {
		this.elements = elements;
	}

	public TelemetryMessage(final TelemetryElement... elements) {
		this(Set.of(elements));
	}

	public void add(final TelemetryElement element) {
		elements.add(element);
	}

	public void add(@NonNull final TelemetryMessage message){
		elements.addAll(message.elements);
	}

	public Set <TelemetryElement> getElements() {
		return elements;
	}
}
