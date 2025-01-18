package org.firstinspires.ftc.teamcode.message;

public class DriveBufMessage implements Message {
	public final double valX, valY, valTurn;

	public DriveBufMessage(double valX, double valY, double valTurn){
		this.valX = valX;
		this.valY = valY;
		this.valTurn = valTurn;
	}
	public DriveBufMessage(double globalBuf){
		this(globalBuf,globalBuf,globalBuf);
	}
}
