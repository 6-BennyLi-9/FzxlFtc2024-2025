package org.betastudio.ftc.ui.telemetry;

public class SelectionTeleElement extends TelemetryItem{
	private boolean selected;

	public SelectionTeleElement(final String title) {
		super("[ ]",title);
	}

	public void press(){
		selected =!selected;
		capital = selected? "[X]" : "[ ]";
	}

	public String getTitle() {
		return value;
	}

	public boolean isSelected() {
		return selected;
	}
}
