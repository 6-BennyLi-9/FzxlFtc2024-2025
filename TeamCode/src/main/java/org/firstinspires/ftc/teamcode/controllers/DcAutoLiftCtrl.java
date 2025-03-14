package org.firstinspires.ftc.teamcode.controllers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.HardwareDatabase;

public class DcAutoLiftCtrl extends DcTeleLiftCtrl {
	public DcAutoLiftCtrl(@NonNull final DcMotorEx leftLift, @NonNull final DcMotorEx rightLift, final long targetPosition) {
		this(leftLift, rightLift, targetPosition, false);
	}

	public DcAutoLiftCtrl(@NonNull final DcMotorEx leftLift, @NonNull final DcMotorEx rightLift, final long targetPosition, final boolean using_touch_calibrate) {
		super(leftLift, rightLift);
		setTargetPosition(targetPosition);
		this.using_touch_calibrate = using_touch_calibrate;
	}

	@Override
	public boolean activate() {
		super.activate();
		return Math.abs(targetPosition - currentPosition) > tolerance || (using_touch_calibrate && 0 == getTargetPosition() && HardwareDatabase.liftTouch.isPressed());
	}
}
