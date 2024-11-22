package org.firstinspires.ftc.teamcode.autonomous.utils.structure;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Deprecated
public class LinearLiftController extends AutonomousLiftController{
	public LinearLiftController(@NonNull final DcMotorEx target, final long targetPosition) {
		super(target, targetPosition);
	}

	@Override
	public boolean run() {
		targetLift.setTargetPosition((int)getTargetPosition());

		if(DcMotor.RunMode.RUN_TO_POSITION != targetLift.getMode()){
			targetLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		}

		return false;
	}

	@Override public boolean getCalibrationDone() {return false;}
	@Override public void modify() {}
	@Override public double getCalibrateVal() {return 0;}
}
