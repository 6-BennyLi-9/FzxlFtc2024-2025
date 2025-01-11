package org.firstinspires.ftc.opmodes.autonomous.structure;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.controllers.lift.DcLiftCtrl;
import org.firstinspires.ftc.teamcode.HardwareDatabase;

public class DcAutoLiftCtrl extends DcLiftCtrl {
	public DcAutoLiftCtrl(@NonNull final DcMotorEx target, final long targetPosition) {
		this(target, targetPosition, false);
	}

	public DcAutoLiftCtrl(@NonNull final DcMotorEx target, final long targetPosition, final boolean using_touch_calibrate) {
		super(target);
		setTargetPosition(targetPosition);
		using_touch_calibrate(using_touch_calibrate);
	}

	@Override
	public boolean run() {
		super.run();
		return (using_touch_calibrate() && 0 == getTargetPosition() && HardwareDatabase.liftTouch.isPressed());
	}
}
