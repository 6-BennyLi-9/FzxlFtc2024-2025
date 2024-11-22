package org.firstinspires.ftc.teamcode.structure.controllers.lift;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.structure.controllers.LiftController;

public class DcLiftController extends LiftController {
	public static double bufPow=1,tolerance=10;

	public DcLiftController(@NonNull final DcMotorEx target) {
		super(target);
	}

	@Override
	public boolean run() {
		targetLift.setTargetPositionTolerance(10);
		targetLift.setTargetPosition((int) getTargetPosition());
		targetLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		targetLift.setPower(bufPow * Math.signum(getErrorPosition()));

		return true;
	}

	@Override public void modify() {}
	@Override public double getCalibrateVal() {return 0;}
}
