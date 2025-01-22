package org.betastudio.ftc.message;

import androidx.annotation.NonNull;

public class DriveBufMessage implements Message {
	public final double valX, valY, valTurn;

	public DriveBufMessage(final double valX, final double valY, final double valTurn){
		this.valX = valX;
		this.valY = valY;
		this.valTurn = valTurn;
	}
	public DriveBufMessage(final double globalBuf){
		this(globalBuf,globalBuf,globalBuf);
	}

	@NonNull
	@Override
	public String toString() {
		return String.format("x=%s, y=%s, turn=%s", valX, valY, valTurn);
	}
}
