package org.firstinspires.ftc.teamcode.controllers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.msg.ChassisCtrlValsMessage;

import java.util.Locale;

public class ChassisCtrl implements Action {
	public final DcMotorEx leftFront, leftRear, rightFront, rightRear;
	private double pX, pY, pTurn;
	private String tag;

	public ChassisCtrl(final DcMotorEx leftFront, final DcMotorEx leftRear, final DcMotorEx rightFront, final DcMotorEx rightRear) {
		this.leftFront = leftFront;
		this.leftRear = leftRear;
		this.rightFront = rightFront;
		this.rightRear = rightRear;
	}

	@Override
	public boolean run() {
		leftFront.setPower(pY - pX - pTurn);
		leftRear.setPower(pY + pX - pTurn);
		rightFront.setPower(pY + pX + pTurn);
		rightRear.setPower(pY - pX + pTurn);
		return true;
	}

	@Override
	public String paramsString() {
		return String.format(Locale.SIMPLIFIED_CHINESE, "%s:{x:%.3f,y%.4f,heading:%.3f}", tag, pX, pY, pTurn);
	}

	public void setPowers(final double x, final double y, final double turn) {
		setPowers(x, y, turn, 1);
	}

	public void setPowers(final double x, final double y, final double turn, final double bufVal) {
		pX = x * bufVal;
		pY = y * bufVal;
		pTurn = turn * bufVal;
	}

	public String getTag() {
		return this.tag;
	}

	public void setTag(final String tag) {
		this.tag = tag;
	}

	public void setPowers(@NonNull ChassisCtrlValsMessage message) {
		pX = message.pX;
		pY = message.pY;
		pTurn = message.pAngle;
	}
}
