package org.firstinspires.ftc.teamcode.autonomous.utils.structure;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.structure.controllers.lift.DcLiftCtrl;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;

public class DcAutoLiftCtrl extends DcLiftCtrl {

	public DcAutoLiftCtrl(@NonNull final DcMotorEx target, final long targetPosition) {
		super(target);
		setTargetPosition(targetPosition);
		using_touch_calibrate=false;
	}

	@Override
	public boolean run() {
		super.run();
		return (getTargetPosition()==0 && HardwareConstants.liftTouch.isPressed());
	}
}
