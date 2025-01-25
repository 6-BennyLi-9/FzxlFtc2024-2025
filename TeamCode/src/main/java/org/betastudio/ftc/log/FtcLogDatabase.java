package org.betastudio.ftc.log;

import androidx.annotation.NonNull;

import org.betastudio.ftc.util.message.TelemetryMessage;
import org.betastudio.ftc.telemetry.TelemetryItem;
import org.betastudio.ftc.specification.MessagesProcessRequired;
import org.betastudio.ftc.util.Timestamp;

import java.util.Set;
import java.util.TreeSet;

public class FtcLogDatabase implements MessagesProcessRequired<TelemetryMessage> {
	private final Set <FtcLogElement> elements;
	public FtcLogDatabase(){
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

	public void addElement(final FtcLogElement element){
		elements.add(element);
	}

	@Override
	public void send(@NonNull final TelemetryMessage message) {
		throw new UnsupportedOperationException("FtcLogDatabase only can been called for TelemetryMessage");
	}

	@Override
	public TelemetryMessage call() {
		final TelemetryMessage result =new TelemetryMessage();
		for (final FtcLogElement element : elements) {
			result.add(new TelemetryItem(
					String.format("[%s]",element.getType().caption),
					String.format("%s||%s",element.getTimestamp(),element.getMessage().toString())
			));
		}
		return result;
	}
}
