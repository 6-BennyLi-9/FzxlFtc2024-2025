package org.firstinspires.ftc.teamcode.autonomous.utils.structure;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class DcLinearLiftController extends AutonomousLiftController{
	public static double bufPow=1;
	public static int tolerance=10;

	public DcLinearLiftController(@NonNull final DcMotorEx target, final long targetPosition) {
		super(target, targetPosition);
	}

	@Override
	public boolean run() {
		targetLift.setTargetPositionTolerance(tolerance);
		targetLift.setTargetPosition((int) getTargetPosition());
		targetLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		targetLift.setPower(bufPow * Math.signum(getErrorPosition()));

		return false;
	}

	@Override public boolean getCalibrationDone() {return false;}
	@Override public void modify() {}
	@Override public double getCalibrateVal() {return 0;}
}
