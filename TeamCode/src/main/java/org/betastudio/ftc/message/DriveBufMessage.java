package org.betastudio.ftc.message;

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
}
