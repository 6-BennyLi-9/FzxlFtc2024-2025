package org.firstinspires.ftc.teamcode.controllers;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.HardwareDatabase;

/**
 * 使用 {@link DcMotorEx} 为基础的控制器
 */
@Config
public class DcTeleLiftCtrl extends LiftCtrl {
	public static final double  bufPow                = 1;
	public static final int     tolerance             = 10;
	private             boolean using_touch_calibrate = true;

	public DcTeleLiftCtrl(@NonNull final DcMotorEx leftLift, @NonNull final  DcMotorEx rightLift) {
		super(leftLift, rightLift);
	}

	@Override
	public boolean activate() {
		currentPosition = rightLift.getCurrentPosition();

		//特殊处理目标值为0的情况
		if (0 == getTargetPosition() && using_touch_calibrate) {
			leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
			leftLift.setPower(! HardwareDatabase.liftTouch.isPressed() ? 0 : - 1);
			rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
			rightLift.setPower(! HardwareDatabase.liftTouch.isPressed() ? 0 : - 1);
			if (! HardwareDatabase.liftTouch.isPressed()) {
				leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
				rightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			}
			return true;
		}

		leftLift.setTargetPosition((int) getTargetPosition());
		leftLift.setTargetPositionTolerance(tolerance);
		leftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		leftLift.setPower(bufPow);
		rightLift.setTargetPosition((int) getTargetPosition());
		rightLift.setTargetPositionTolerance(tolerance);
		rightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		rightLift.setPower(bufPow);

		return true;
	}

	public void using_touch_calibrate(final boolean using_touch_calibrate) {
		this.using_touch_calibrate = using_touch_calibrate;
	}

	public boolean using_touch_calibrate() {
		return using_touch_calibrate;
	}
}
