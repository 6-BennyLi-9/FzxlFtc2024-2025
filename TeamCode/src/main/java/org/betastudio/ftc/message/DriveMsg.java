package org.betastudio.ftc.message;

import androidx.annotation.NonNull;

import java.util.Locale;

public class DriveMsg implements Message {
	public final double valX, valY, valTurn;

	public DriveMsg(final double valX, final double valY, final double valTurn) {
		this.valX = valX;
		this.valY = valY;
		this.valTurn = valTurn;
	}

	public DriveMsg(final double globalBuf) {
		this(globalBuf, globalBuf, globalBuf);
	}

	@NonNull
	@Override
	public String toString() {
		return String.format(Locale.SIMPLIFIED_CHINESE,"x=%.2f, y=%.2f, turn=%.2f", valX, valY, valTurn);
	}
}
