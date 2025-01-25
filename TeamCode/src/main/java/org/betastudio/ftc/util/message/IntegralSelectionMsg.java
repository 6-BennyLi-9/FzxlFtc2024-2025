package org.betastudio.ftc.util.message;

import android.util.Range;

import org.betastudio.ftc.ui.telemetry.SelectionTeleElement;

public class IntegralSelectionMsg extends SelectionTelemetryMsg{
	private int showLinesCount;
	private Range <Integer> showRange;

	public IntegralSelectionMsg(final SelectionTeleElement... elements) {
		super(elements);
		showLinesCount = 10;
		showRange=new Range<>(0,showLinesCount);
	}

	public void setShowLinesCount(final int showLinesCount) {
		this.showLinesCount = showLinesCount;
		showRange=new Range<>(showRange.getLower(),showRange.getLower()+showLinesCount);
	}

	public void setShowLowerBound(final int showLowerBound) {
		showRange=new Range<>(showLowerBound,showLowerBound+showLinesCount);
	}

	public void setShowUpperBound(final int showUpperBound) {
		showRange=new Range<>(showUpperBound-showLinesCount,showUpperBound);
	}

	@Override
	public TelemetryMsg convertToTelemetryMsg() {
		final TelemetryMsg telemetryMsg = new TelemetryMsg();
		for (int i = showRange.getLower(); i < showRange.getUpper(); i++){
			telemetryMsg.add(getSelectionElements().get(i));
		}
		return telemetryMsg;
	}
}
