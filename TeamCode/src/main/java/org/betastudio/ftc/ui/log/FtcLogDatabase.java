package org.betastudio.ftc.ui.log;

import androidx.annotation.NonNull;

import org.betastudio.ftc.util.message.TelemetryMessage;
import org.betastudio.ftc.ui.telemetry.TelemetryItem;
import org.betastudio.ftc.specification.MessagesProcessRequired;
import org.betastudio.ftc.util.time.Timer;
import org.betastudio.ftc.util.time.Timestamp;

import java.util.Set;
import java.util.TreeSet;

public class FtcLogDatabase implements MessagesProcessRequired <TelemetryMessage> {
	private final Set <FtcLogElement> elements;
	private double saveTime;
	private boolean saved;

	public FtcLogDatabase() {
		elements = new TreeSet <>((o1, o2) -> {
			final Timestamp t1 = o1.getTimestamp();
			final Timestamp t2 = o2.getTimestamp();
			if (t1.getMinute().equals(t2.getMinute())) {
				return Integer.compare(Integer.parseInt(t1.getSecond()), Integer.parseInt(t2.getSecond()));
			} else {
				return Integer.compare(Integer.parseInt(t1.getMinute()), Integer.parseInt(t2.getMinute()));
			}
		});
	}

	public void addElement(final FtcLogElement element) {
		elements.add(element);
	}

	public Set <FtcLogElement> getElements() {
		return elements;
	}

	@Override
	public void send(@NonNull final TelemetryMessage message) {
		throw new UnsupportedOperationException("FtcLogDatabase only can been called for TelemetryMessage");
	}

	@Override
	public TelemetryMessage call() {
		final TelemetryMessage result = new TelemetryMessage();
		for (final FtcLogElement element : elements) {
			result.add(new TelemetryItem(String.format("[%s]", element.getType().caption), String.format("<%s>%s", element.getTimestamp(), element.getMessage().toString())));
		}
		return result;
	}

	public FtcLogDatabase save(){
		if(saved){
			throw new IllegalLogSaveOptionException("Log has already been saved");
		}
		saveTime = Timer.getCurrentTime();
		saved = true;
		return this;
	}

	public double getSaveTime() {
		if(!saved){
			throw new IllegalLogSaveOptionException("Log has not been saved yet");
		}
		return saveTime;
	}
}
