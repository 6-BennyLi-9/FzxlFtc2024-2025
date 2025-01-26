package org.betastudio.ftc.util.message;

import androidx.annotation.NonNull;

import org.betastudio.ftc.ui.telemetry.TelemetryElement;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TelemetryMsg implements Message{
	private final Set<TelemetryElement> elements;

	public TelemetryMsg(final Set<TelemetryElement> elements) {
		this.elements = elements;
	}

	public TelemetryMsg(final TelemetryElement... elements) {
		this(new HashSet <>(Arrays.asList(elements)));
	}

	public void add(final TelemetryElement element) {
		elements.add(element);
	}

	public void addAll(Collection<TelemetryElement> elements){
		this.elements.addAll(elements);
	}

	public Set <TelemetryElement> getElements() {
		return elements;
	}

	public void merge(@NonNull final TelemetryMsg message){
		elements.addAll(message.elements);
	}
}
