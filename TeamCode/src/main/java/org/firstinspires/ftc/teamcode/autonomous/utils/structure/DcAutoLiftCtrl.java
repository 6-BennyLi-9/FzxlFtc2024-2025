package org.firstinspires.ftc.teamcode.autonomous.utils.structure;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.structure.controllers.LiftCtrl;

public class DcAutoLiftCtrl extends LiftCtrl {
	public static double bufPow=1;
	public static int tolerance=10;

	public DcAutoLiftCtrl(@NonNull final DcMotorEx target, final long targetPosition) {
		super(target);
		setTargetPosition(targetPosition);
	}

	@Override
	public boolean run() {
		targetLift.setTargetPositionTolerance(tolerance);
		targetLift.setTargetPosition((int) getTargetPosition());
		targetLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		targetLift.setPower(bufPow);

		return false;
	}

	@Override public boolean getCalibrateDone() {return false;}
	@Override public void modify() {}
	@Override public double getCalibrateVal() {return 0;}
}
