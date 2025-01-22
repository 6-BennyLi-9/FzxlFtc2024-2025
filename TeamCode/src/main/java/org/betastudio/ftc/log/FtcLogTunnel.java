package org.betastudio.ftc.log;

import androidx.annotation.NonNull;

import org.betastudio.ftc.message.StringMessage;
import org.betastudio.ftc.message.TelemetryMessage;

public enum FtcLogTunnel {
	MAIN;
	private final FtcLogDatabase log = new FtcLogDatabase();

	public void report(final String s){
		log.addElement(new FtcLogElement.ElementImpl(new StringMessage(s)));
	}

	public void report(@NonNull final Throwable e){
		log.addElement(new FtcLogElement.ElementImpl(new StringMessage(e.getMessage())));
	}

	public void report(final FtcLogElement element){
		log.addElement(element);
	}

	public TelemetryMessage call(){
		return log.call();
	}
}
