package org.betastudio.ftc.ui.telemetry;

public class IntegralSelectElement extends SelectionTeleElement {
	private Runnable onSelected;

	public IntegralSelectElement(final String title, final Runnable onSelected) {
		super(title);
		this.onSelected = onSelected;
	}

	public void setOnSelected(final Runnable onSelected) {
		this.onSelected = onSelected;
	}

	public void submit(){
		assert isSelected();
		onSelected.run();
	}
}
