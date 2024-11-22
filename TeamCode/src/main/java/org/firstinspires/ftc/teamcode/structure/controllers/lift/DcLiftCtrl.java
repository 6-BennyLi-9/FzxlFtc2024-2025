package org.firstinspires.ftc.teamcode.structure.controllers.lift;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.structure.controllers.LiftCtrl;

public class DcLiftCtrl extends LiftCtrl {
	public static double bufPow=1;
	public static int tolerance=10;

	public DcLiftCtrl(@NonNull final DcMotorEx target) {
		super(target);
	}

	@Override
	public boolean run() {
		targetLift.setTargetPositionTolerance(tolerance);
		targetLift.setTargetPosition((int) getTargetPosition());
		targetLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		targetLift.setPower(bufPow * Math.signum(getErrorPosition()));

		return true;
	}

	@Override public void modify() {}
	@Override public double getCalibrateVal() {return 0;}
}
