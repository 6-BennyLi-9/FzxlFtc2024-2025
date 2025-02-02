package org.betastudio.ftc.selection;

import android.util.Range;

import androidx.annotation.NonNull;

import org.betastudio.ftc.util.entry.Updatable;
import org.betastudio.ftc.util.message.TelemetryMsg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectPackage implements Updatable {
	private final List <SelectElement> elements;
	private       int                  selected_index, show_lines;
	private Range <Integer> show_range;

	{
		selected_index = 0;
		show_lines = 10;
		show_range = Range.create(0, show_lines);
	}

	public SelectPackage(@NonNull final List <SelectElement> elements) {
		this.elements = elements;
		if (! elements.isEmpty()) {
			elements.get(selected_index).setSelected(true);
		}
	}

	public SelectPackage(final SelectElement... elements) {
		this(new ArrayList <>(Arrays.asList(elements)));
	}

	public void add(final SelectElement element) {
		elements.add(element);
	}

	public void set_show_lines(final int lines) {
		show_lines = lines;
		show_range = Range.create(0, show_lines);
	}

	public void set_show_lower(final int lower_index) {
		show_range = Range.create(lower_index, lower_index + show_lines);
	}

	public void toggleSelected(final int index) {
		assert 0 < index && index < elements.size();
		elements.get(index).toggleSelected();
	}

	public void select_previous() {
		elements.get(selected_index).setSelected(false);
		selected_index = (selected_index - 1 + elements.size()) % elements.size();
		elements.get(selected_index).setSelected(true);
	}

	public void select_next() {
		elements.get(selected_index).setSelected(false);
		selected_index = (selected_index + 1 + elements.size()) % elements.size();
		elements.get(selected_index).setSelected(true);
	}

	public TelemetryMsg buildTelemetryMsg() {
		final TelemetryMsg msg = new TelemetryMsg();
		for (int i = show_range.getLower() ; i < elements.size() && i < show_range.getUpper() ; i++) {
			msg.add(elements.get(i).buildTelemetryItem());
		}
		return msg;
	}

	public List <SelectElement> getElements() {
		return elements;
	}

	public Range <Integer> getShow_range() {
		return show_range;
	}

	public void submit_selected() {
		elements.get(selected_index).submit();
	}

	@Override
	public void update() {
		for (int i = 0 ; i < elements.size() ; i++) {
			elements.get(i).setSelected(i == selected_index);
		}
	}

	@Override
	public boolean isUpdateRequested() {
		return false;
	}
}
