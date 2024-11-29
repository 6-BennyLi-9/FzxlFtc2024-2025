package org.firstinspires.ftc.teamcode.autonomous.structure;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.structure.controllers.lift.DcLiftCtrl;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;

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
		return (get_using_touch_calibrate() && 0 == getTargetPosition() && HardwareConstants.liftTouch.isPressed());
	}
}
