package org.firstinspires.ftc.teamcode.controllers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.HardwareConfigures;
import org.firstinspires.ftc.teamcode.Hardwares;

public class DcAutoLiftCtrl extends DcTeleLiftCtrl {
	public DcAutoLiftCtrl(@NonNull final DcMotorEx leftLift, @NonNull final DcMotorEx rightLift, final int targetPosition) {
		this(leftLift, rightLift, targetPosition, false);
	}

	public DcAutoLiftCtrl(@NonNull final DcMotorEx leftLift, @NonNull final DcMotorEx rightLift, final int targetPosition, final boolean using_touch_calibrate) {
		super(leftLift, rightLift);
		setTargetPosition(targetPosition);
		this.using_touch_calibrate = using_touch_calibrate;
	}

	@Override
	public boolean activate() {
		super.activate();
		return HardwareConfigures.LIFT_TOLERANCE < Math.abs(targetPosition - currentPosition) || (using_touch_calibrate && 0 == getTargetPosition() && Hardwares.liftTouch.isPressed());
	}
}
