package org.firstinspires.ftc.teamcode.controllers;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.interfaces.DashboardCallable;

import java.util.Locale;

public class ChassisCtrl implements Action , DashboardCallable {
	public static double kS=-1,kF=1;
	public final DcMotorEx leftFront, leftRear, rightFront, rightRear;
	public ClassicCtrlMode mode = ClassicCtrlMode.NONE_SPECIFIED;
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
		switch (mode){
			case FASTER_CONTROL:
				pX=resolveFunc(pX,kF);
				pY=resolveFunc(pY,kF);
				pTurn=resolveFunc(pTurn,kF);
				break;
			case SLOWER_CONTROL:
				pX=resolveFunc(pX,kS);
				pY=resolveFunc(pY,kS);
				pTurn=resolveFunc(pTurn,kS);
				break;
			case NONE_SPECIFIED:
			default:
				break;
		}

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

	@Override
	public void send(@NonNull TelemetryPacket packet) {
		packet.put("drive-x",pX);
		packet.put("drive-y",pY);
		packet.put("drive-turn",pTurn);
	}

	private double resolveFunc(double val, double k) {
		double result = k * val * val + (1 - k) * val;
		if (Math.signum(result) != Math.signum(val)){
			result = -result;
		}
		return result;
	}
}
