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

	public DcTeleLiftCtrl(@NonNull final DcMotorEx target) {
		super(target);
	}

	@Override
	public boolean activate() {
		currentPosition = targetLift.getCurrentPosition();

		//特殊处理目标值为0的情况
		if (0 == getTargetPosition() && using_touch_calibrate) {
			targetLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
			targetLift.setPower(! HardwareDatabase.liftTouch.isPressed() ? 0 : - 1);
			if (! HardwareDatabase.liftTouch.isPressed()) {
				targetLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			}
			return true;
		}

		targetLift.setTargetPosition((int) getTargetPosition());
		targetLift.setTargetPositionTolerance(tolerance);
		targetLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		targetLift.setPower(bufPow);

		return true;
	}

	public void using_touch_calibrate(final boolean using_touch_calibrate) {
		this.using_touch_calibrate = using_touch_calibrate;
	}

	public boolean using_touch_calibrate() {
		return using_touch_calibrate;
	}
}
