package org.betastudio.ftc.ui.log;

import androidx.annotation.NonNull;

import org.betastudio.ftc.specification.MessagesProcessRequired;
import org.betastudio.ftc.ui.telemetry.TelemetryItem;
import org.betastudio.ftc.util.Labeler;
import org.betastudio.ftc.util.message.StringMsg;
import org.betastudio.ftc.util.message.TelemetryMsg;
import org.betastudio.ftc.util.time.Timestamp;

import java.util.Set;
import java.util.TreeSet;

public class FtcLogFile implements MessagesProcessRequired <TelemetryMsg> {
	private final Set <FtcLogElement> elements;
	private Timestamp saveTime;
	private boolean saved;
	private String fileName;

	public FtcLogFile() {
		elements = new TreeSet <>((o2, o1) -> {
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
	public void send(@NonNull final TelemetryMsg message) {
		throw new UnsupportedOperationException("FtcLogFile only can been called for TelemetryMsg");
	}

	@Override
	public TelemetryMsg call() {
		final TelemetryMsg result = new TelemetryMsg();
		for (FtcLogElement element : elements) {
			result.add(new TelemetryItem(String.format("[%s]", element.getType().caption), String.format("<%s>%s", element.getTimestamp(), element.getMessage().toString())));
		}
		return result;
	}

	public FtcLogFile save(){
		if(saved){
			throw new IllegalLogSaveOptionException("Log has already been saved");
		}
		addElement(new FtcLogElement.ElementImpl(new StringMsg("EOF")));
		saveTime = new Timestamp();
		saved = true;
		setFileName(Labeler.generate().summonID(this)+saveTime);
		return this;
	}

	public Timestamp getSaveTime() {
		if(!saved){
			throw new IllegalLogSaveOptionException("Log has not been saved yet");
		}
		return saveTime;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}
}
