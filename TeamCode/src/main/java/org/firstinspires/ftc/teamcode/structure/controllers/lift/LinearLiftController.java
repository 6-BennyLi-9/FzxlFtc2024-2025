package org.firstinspires.ftc.teamcode.structure.controllers.lift;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.structure.controllers.LiftController;

@Deprecated
@Config
public class LinearLiftController extends LiftController {
	public static double p=10,i=3,d,f=7;

	public LinearLiftController(@NonNull DcMotorEx target) {
		super(target);
	}

	@Override
	public boolean run() {
		targetLift.setTargetPosition((int)getTargetPosition());

		if(targetLift.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
			targetLift.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION,new PIDFCoefficients(p,i,d,f));
		}

		return true;
	}

	@Override public final void modify() {}
	@Override public final double getCalibrateVal() {return 0;}
}
