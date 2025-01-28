package org.betastudio.ftc.util.selection;

import org.betastudio.ftc.ui.telemetry.TelemetryLine;

public class SelectElement {
	private final String name;
	private final Runnable on_select;
	private boolean is_selected;

	public SelectElement(final String name, final Runnable on_select) {
		this.name = name;
		this.on_select = on_select;
	}

	public String getName() {
		return name;
	}

	public void submit(){
		on_select.run();
	}

	public boolean isSelected() {
		return is_selected;
	}

	public void setSelected(final boolean selected) {
		is_selected = selected;
	}

	public void toggleSelected() {
		is_selected =!is_selected;
	}

	public TelemetryLine buildTelemetryItem() {
		return new TelemetryLine((is_selected? "[X] ":"[  ] ") + name);
	}
}
