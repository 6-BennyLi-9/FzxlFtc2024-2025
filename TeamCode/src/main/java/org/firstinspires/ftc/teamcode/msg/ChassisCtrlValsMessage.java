package org.firstinspires.ftc.teamcode.msg;

public class ChassisCtrlValsMessage {
	public final double pX,pY,pAngle;

	public ChassisCtrlValsMessage() {
		this(0,0,0);
	}
	public ChassisCtrlValsMessage(double pX, double pY, double pAngle) {
		this.pX = pX;
		this.pY = pY;
		this.pAngle = pAngle;
	}
}
