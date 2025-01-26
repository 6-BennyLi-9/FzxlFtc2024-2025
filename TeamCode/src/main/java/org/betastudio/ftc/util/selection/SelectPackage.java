package org.betastudio.ftc.util.selection;

import android.util.Range;

import androidx.annotation.NonNull;

import org.betastudio.ftc.util.message.TelemetryMsg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectPackage {
	private final List <SelectElement> elements;
	private int selected_index,show_lines;
	private Range<Integer> show_range;

	{
		selected_index = 0;
		show_lines = 10;
		show_range = Range.create(0, show_lines);
	}

	public SelectPackage(@NonNull List <SelectElement> elements) {
		this.elements = elements;
		if(! elements.isEmpty()) {
			elements.get(selected_index).setSelected(true);
		}
	}

	public SelectPackage(SelectElement... elements) {
		this(new ArrayList <>(Arrays.asList(elements)));
	}

	public void add(SelectElement element) {
		elements.add(element);
	}

	public void set_show_lines(int lines){
		show_lines = lines;
		show_range = Range.create(0, show_lines);
	}

	public void set_show_lower(int lower_index){
		show_range = Range.create(lower_index, lower_index+show_lines);
	}

	public void toggleSelected(int index) {
		assert  index > 0 && index < elements.size();
		elements.get(index).toggleSelected();
	}

	public void select_previous() {
		elements.get(selected_index).setSelected(false);
		selected_index = (selected_index - 1 + elements.size()) % elements.size();
		elements.get(selected_index).setSelected(true);
	}

	public void select_next() {
		elements.get(selected_index).setSelected(false);
		selected_index = (selected_index +1 + elements.size()) % elements.size();
		elements.get(selected_index).setSelected(true);
	}

	public TelemetryMsg buildTelemetryMsg() {
		TelemetryMsg msg = new TelemetryMsg();
//		for (int i = show_range.getLower() ; i < elements.size() && i < show_range.getUpper() ; i++) {
//			msg.add(elements.get(i).buildTelemetryItem());
//		}
		elements.forEach(element -> msg.add(element.buildTelemetryItem()));
		return msg;
	}

	public List <SelectElement> getElements() {
		return elements;
	}

	public Range <Integer> getShow_range() {
		return show_range;
	}
}
