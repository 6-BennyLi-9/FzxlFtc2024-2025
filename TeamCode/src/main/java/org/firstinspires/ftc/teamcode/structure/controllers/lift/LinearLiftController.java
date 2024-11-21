package org.firstinspires.ftc.teamcode.structure.controllers.lift;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.structure.controllers.LiftController;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;

public class LinearLiftController extends LiftController {
	protected LinearLiftController(@NonNull DcMotorEx target) {
		super(target);
		target.setMode(DcMotor.RunMode.RUN_TO_POSITION);
	}

	@Override
	public boolean run() {
		if(targetLift.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
			targetLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		}

		targetLift.setTargetPosition((int)getTargetPosition());

		return true;
	}

	@Override
	public final void modify() {}

	@Override
	public final double getCalibrateVal() {return 0;}
}
