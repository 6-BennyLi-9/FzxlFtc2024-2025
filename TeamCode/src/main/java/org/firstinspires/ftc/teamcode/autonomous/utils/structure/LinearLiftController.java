package org.firstinspires.ftc.teamcode.autonomous.utils.structure;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class LinearLiftController extends AutonomousLiftController{
	protected LinearLiftController(@NonNull DcMotorEx target, long targetPosition) {
		super(target, targetPosition);
		target.setMode(DcMotor.RunMode.RUN_TO_POSITION);
	}

	@Override
	public boolean run() {
		if(targetLift.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
			targetLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		}

		targetLift.setTargetPosition((int)getTargetPosition());

		return false;
	}

	@Override public boolean getCalibrationDone() {return false;}
	@Override public void modify() {}
	@Override public double getCalibrateVal() {return 0;}
}
