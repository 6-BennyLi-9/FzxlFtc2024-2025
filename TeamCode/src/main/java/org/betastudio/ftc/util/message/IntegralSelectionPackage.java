package org.betastudio.ftc.util.message;

import android.util.Range;

import org.betastudio.ftc.ui.telemetry.SelectionTeleElement;

public class IntegralSelectionPackage extends SelectionTelemetryMsg{
	private int show_lines_count,now_selected_index;
	private Range <Integer> showRange;

	public IntegralSelectionPackage(final SelectionTeleElement... elements) {
		super(elements);
		show_lines_count = 10;
		showRange=new Range<>(0, show_lines_count);
		now_selected_index = 0;
	}

	public void setShow_lines_count(final int show_lines_count) {
		this.show_lines_count = show_lines_count;
		showRange=new Range<>(showRange.getLower(), showRange.getLower() + show_lines_count);
	}

	public void setShowLowerBound(int showLowerBound) {
		showLowerBound = Math.max(0, showLowerBound);
		showRange=new Range<>(showLowerBound, showLowerBound + show_lines_count);
	}

	public void setShowUpperBound(final int showUpperBound) {
		showRange=new Range<>(showUpperBound - show_lines_count,showUpperBound);
	}

	public void select_prev(){
		if (0 < now_selected_index) {
			elements.get(now_selected_index).press();
			now_selected_index--;
			elements.get(now_selected_index).press();
		}
	}

	public void select_next(){
		if (showRange.getUpper() > now_selected_index) {
			elements.get(now_selected_index).press();
			now_selected_index++;
			elements.get(now_selected_index).press();
		}
	}

	public void submit() {
		submit(now_selected_index);
	}

	@Override
	public TelemetryMsg convertToTelemetryMsg() {
		final TelemetryMsg telemetryMsg = new TelemetryMsg();
		for (int i = showRange.getLower(); i < showRange.getUpper() && i < getElements().size(); i++){
			telemetryMsg.add(getElements().get(i));
		}
		return telemetryMsg;
	}
}
