package org.firstinspires.ftc.teamcode.message;

public class DriveBufMessage {
	public double bufX,bufY,bufTurn;

	public DriveBufMessage(double bufX,double bufY,double bufTurn){
		this.bufX=bufX;
		this.bufY=bufY;
		this.bufTurn=bufTurn;
	}
	public DriveBufMessage(double globalBuf){
		this(globalBuf,globalBuf,globalBuf);
	}
}
