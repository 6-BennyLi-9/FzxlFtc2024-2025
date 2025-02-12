package org.betastudio.ftc.util.message;

import androidx.annotation.NonNull;

public class DriveBufMsg implements Message {
	public final double valX, valY, valTurn;

	public DriveBufMsg(final double valX, final double valY, final double valTurn){
		this.valX = valX;
		this.valY = valY;
		this.valTurn = valTurn;
	}
	public DriveBufMsg(final double globalBuf){
		this(globalBuf,globalBuf,globalBuf);
	}

	@NonNull
	@Override
	public String toString() {
		return String.format("x=%s, y=%s, turn=%s", valX, valY, valTurn);
	}
}
