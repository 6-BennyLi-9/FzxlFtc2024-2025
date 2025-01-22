package org.betastudio.ftc.message;

import androidx.annotation.NonNull;

import org.betastudio.ftc.telemetry.TelemetryElement;

import java.util.Set;

public class TelemetryMessage implements Message{
	public final Set<TelemetryElement> elements;

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
}
