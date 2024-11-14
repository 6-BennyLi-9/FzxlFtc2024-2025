package org.firstinspires.ftc.teamcode.structure.controllers;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.actions.Action;

public class ChassisController implements Action {
	private double pX,pY,pTurn;
	public final DcMotorEx leftFront,leftRear,rightFront,rightRear;
	private String tag;
	
	public ChassisController(final DcMotorEx leftFront, final DcMotorEx leftRear, final DcMotorEx rightFront, final DcMotorEx rightRear){
		this.leftFront=leftFront;
		this.leftRear=leftRear;
		this.rightFront=rightFront;
		this.rightRear=rightRear;
	}
	
	@Override
	public boolean run() {
		leftFront.setPower	(pY+pX-pTurn);
		leftRear.setPower	(pY-pX-pTurn);
		rightFront.setPower	(pY-pX+pTurn);
		rightRear.setPower	(pY+pX+pTurn);
		return true;
	}
	@Override
	public String paramsString() {
		return "x:"+pX+
				",y"+pY+
				",heading:"+pTurn;
	}

	public void setPowers(final double x, final double y, final double turn){
		setPowers(x,y,turn,1);
	}
	public void setPowers(final double x, final double y, final double turn,final double bufVal){
		pX=x*bufVal;
		pY=y*bufVal;
		pTurn=turn*bufVal;
	}
}
