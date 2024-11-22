package org.firstinspires.ftc.teamcode.structure.controllers.lift;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.structure.controllers.LiftController;

public class LinearLiftController extends LiftController {
	protected LinearLiftController(@NonNull final DcMotorEx target) {
		super(target);
		target.setMode(DcMotor.RunMode.RUN_TO_POSITION);
	}

	@Override
	public boolean run() {
		if(DcMotor.RunMode.RUN_TO_POSITION != targetLift.getMode()){
			targetLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		}

		targetLift.setTargetPosition((int)getTargetPosition());

		return true;
	}

	@Override public final void modify() {}
	@Override public final double getCalibrateVal() {return 0;}
}
