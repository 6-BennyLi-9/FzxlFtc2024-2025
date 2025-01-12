package org.firstinspires.ftc.teamcode.controllers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.HardwareDatabase;

public abstract class LiftCtrl implements Action {
	protected final DcMotorEx targetLift;
	protected long currentPosition, targetPosition;
	protected String  tag;
	private   boolean infinityRun = true;

	protected LiftCtrl(@NonNull final DcMotorEx target) {
		targetLift = target;
		tag = target.getDeviceName();
	}


	@Override
	public boolean run() {
		currentPosition = targetLift.getCurrentPosition();

		modify();
		targetLift.setPower(getCalibrateVal());

		if (! HardwareDatabase.liftTouch.isPressed()) {
			targetLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			targetLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		}

		if (infinityRun) return true;
		return ! getCalibrateDone();
	}

	public abstract void modify();

	public abstract double getCalibrateVal();

	/**
	 * @return 校准是否完成
	 */
	public boolean getCalibrateDone() {
		return true;
	}

	@Override
	public String paramsString() {
		return tag + ":" + currentPosition + "->" + targetPosition;
	}

	public String getTag() {
		return this.tag;
	}

	public void setTag(final String tag) {
		this.tag = tag;
	}

	public long getTargetPosition() {
		return targetPosition;
	}

	public void setTargetPosition(final long targetPosition) {
		this.targetPosition = targetPosition;
	}

	public long getCurrentPosition() {
		return currentPosition;
	}

	public long getErrorPosition() {
		return targetPosition - currentPosition;
	}

	protected void disableInfinityRun() {
		this.infinityRun = false;
	}
}
