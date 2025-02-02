package org.betastudio.ftc.ui;

import androidx.annotation.NonNull;

import org.betastudio.ftc.util.Labeler;

public class LoggingObjectToken {
	public final String token;
	public LoggingObjectToken(){
		token = Labeler.generate().summonID(this) + hashCode();
	}

	@NonNull
	@Override
	public String toString() {
		return token;
	}
}
