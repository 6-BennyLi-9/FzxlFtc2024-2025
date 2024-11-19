package org.firstinspires.ftc.teamcode.structure.controllers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.actions.Action;

public abstract class LiftController implements Action {
	private long currentPosition,targetPosition,errorPosition;
	private final DcMotorEx targetLift;

	private String tag;

	protected LiftController(@NonNull final DcMotorEx target){
		targetLift=target;
		tag=target.getDeviceName();
	}


	@Override
	public final boolean run() {
		currentPosition= targetLift	.getCurrentPosition();
		errorPosition=targetPosition- currentPosition;

		modify();
		targetLift.setPower(getCalibrateVal());

		if(! HardwareConstants.liftTouch.isPressed()){
			targetLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			targetLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		}

		return true;
	}

	public abstract void modify();
	public abstract double getCalibrateVal();

	@Override
	public String paramsString() {
		return tag+":"+currentPosition+"->"+targetPosition;
	}

	public void setTargetPosition(final long targetPosition) {
		this.targetPosition = targetPosition;
	}

	public void setTag(final String tag) {
		this.tag = tag;
	}

	public long getTargetPosition() {
		return targetPosition;
	}

	public long getCurrentPosition() {
		return currentPosition;
	}

	public long getErrorPosition() {
		return errorPosition;
	}
}
