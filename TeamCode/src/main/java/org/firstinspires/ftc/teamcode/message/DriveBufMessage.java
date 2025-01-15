package org.firstinspires.ftc.teamcode.message;

public class DriveBufMessage {
	public double valX, valY, valTurn;

	public DriveBufMessage(double valX, double valY, double valTurn){
		this.valX = valX;
		this.valY = valY;
		this.valTurn = valTurn;
	}
	public DriveBufMessage(double globalBuf){
		this(globalBuf,globalBuf,globalBuf);
	}
}
