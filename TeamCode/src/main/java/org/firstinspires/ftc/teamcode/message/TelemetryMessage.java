package org.firstinspires.ftc.teamcode.message;

import org.betastudio.ftc.telemetry.TelemetryElement;

import java.util.Set;

public class TelemetryMessage implements Message{
	public final Set<TelemetryElement> elements;

	public TelemetryMessage(Set<TelemetryElement> elements) {
		this.elements = elements;
	}

	public TelemetryMessage(TelemetryElement... elements) {
		this(Set.of(elements));
	}

	public void add(TelemetryElement element) {
		elements.add(element);
	}
}
