package org.firstinspires.ftc.teamcode.autonomous.utils.structure;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class LinearLiftController extends AutonomousLiftController{
	public LinearLiftController(@NonNull final DcMotorEx target, final long targetPosition) {
		super(target, targetPosition);
		target.setMode(DcMotor.RunMode.RUN_TO_POSITION);
	}

	@Override
	public boolean run() {
		if(DcMotor.RunMode.RUN_TO_POSITION != targetLift.getMode()){
			targetLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		}

		targetLift.setTargetPosition((int)getTargetPosition());

		return false;
	}

	@Override public boolean getCalibrationDone() {return false;}
	@Override public void modify() {}
	@Override public double getCalibrateVal() {return 0;}
}
