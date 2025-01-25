package org.betastudio.ftc.util.message;

import androidx.annotation.NonNull;

import org.betastudio.ftc.ui.telemetry.SelectionTeleElement;
import org.betastudio.ftc.ui.telemetry.TelemetryElement;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class SelectionTeleMessage extends TelemetryMessage{
	private final Set <SelectionTeleElement> elements;

	public SelectionTeleMessage(@NonNull final Set<SelectionTeleElement> elements) {
		this.elements = new TreeSet<>(Comparator.comparing(SelectionTeleElement::getTitle));
		this.elements.addAll(elements);
	}

	public SelectionTeleMessage(final SelectionTeleElement... elements) {
		this(Set.of(elements));
	}

	@Override
	public void add(final TelemetryElement element) {
		assert element instanceof SelectionTeleElement;
		elements.add((SelectionTeleElement) element);
	}

	@Override
	public void add(@NonNull final TelemetryMessage message) {
		assert message instanceof SelectionTeleMessage;
		elements.addAll(((SelectionTeleMessage) message).elements);
	}

	public Set <SelectionTeleElement> getSelectionElements() {
		return elements;
	}

//------------------------
//  UNSUPPORTED METHODS
//------------------------

	@Override
	public Set <TelemetryElement> getElements() {
		throw new UnsupportedOperationException();
	}
}
