package org.firstinspires.ftc.teamcode.controllers;

import static org.firstinspires.ftc.teamcode.HardwareConfigures.AUTO_LIFT_POWER;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.LIFT_TOLERANCE;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Hardwares;

/**
 * 使用 {@link DcMotorEx} 为基础的控制器
 */
public class DcTeleLiftCtrl extends AbstractLiftCtrl {
	protected boolean using_touch_calibrate = true;

	public DcTeleLiftCtrl(@NonNull final DcMotorEx leftLift, @NonNull final DcMotorEx rightLift) {
		super(leftLift, rightLift);
	}

	@Override
	public boolean activate() {
		currentPosition = (rightLift.getCurrentPosition() + leftLift.getCurrentPosition()) / 2;

		//特殊处理目标值为0的情况
		if (0 == getTargetPosition() && using_touch_calibrate) {
			leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
			rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
			leftLift.setPower(! Hardwares.liftTouch.isPressed() ? 0 : - 1);
			rightLift.setPower(! Hardwares.liftTouch.isPressed() ? 0 : - 1);
			if (! Hardwares.liftTouch.isPressed()) {
				leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
				rightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			}
			return true;
		}

		leftLift.setTargetPosition(getTargetPosition());
		rightLift.setTargetPosition(getTargetPosition());
		leftLift.setTargetPositionTolerance(LIFT_TOLERANCE);
		rightLift.setTargetPositionTolerance(LIFT_TOLERANCE);
		leftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		rightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		leftLift.setPower(AUTO_LIFT_POWER);
		rightLift.setPower(AUTO_LIFT_POWER);

		return true;
	}

	@Override
	public String paramsString() {
		return tag + ":(" + rightLift.getCurrentPosition() + ',' + leftLift.getCurrentPosition() + ")->" + targetPosition;
	}
}
