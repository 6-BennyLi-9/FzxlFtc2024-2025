package org.betastudio.ftc.ui;

import androidx.annotation.NonNull;

import org.betastudio.ftc.util.Labeler;

public class LoggingObjectToken implements Runnable{
	public final String token;
	private final Runnable runnable;
	public LoggingObjectToken(final Runnable runnable){
		token = Labeler.generate().summonID(this) + hashCode();
		this.runnable = runnable;
	}

	@NonNull
	@Override
	public String toString() {
		return token;
	}

	@Override
	public void run() {
		runnable.run();
	}
}
