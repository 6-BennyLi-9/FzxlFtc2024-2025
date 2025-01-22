package org.betastudio.ftc.log;

import androidx.annotation.NonNull;

import org.betastudio.ftc.interfaces.MessagesProcessRequired;
import org.betastudio.ftc.message.TelemetryMessage;
import org.betastudio.ftc.telemetry.TelemetryItem;

import java.util.Comparator;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

public class FtcLogDatabase implements MessagesProcessRequired<TelemetryMessage> {
	private final Set <FtcLogElement> elements;
	public FtcLogDatabase(){
		elements=new TreeSet <>(Comparator.comparingDouble(FtcLogElement::getTimestamp));
	}

	public void addElement(final FtcLogElement element){
		elements.add(element);
	}

	@Override
	public void sendRequest(@NonNull final TelemetryMessage message) {
		throw new UnsupportedOperationException("FtcLogDatabase only can been called for TelemetryMessage");
	}

	@Override
	public TelemetryMessage call() {
		final TelemetryMessage result =new TelemetryMessage();
		for (final FtcLogElement element : elements) {
			result.add(new TelemetryItem(
					String.format(Locale.SIMPLIFIED_CHINESE,"[%s]",element.getType().caption),
					String.format(Locale.SIMPLIFIED_CHINESE,"%2.2f||%s",element.getTimestamp(),element.getMessage().toString())
			));
		}
		return result;
	}
}
