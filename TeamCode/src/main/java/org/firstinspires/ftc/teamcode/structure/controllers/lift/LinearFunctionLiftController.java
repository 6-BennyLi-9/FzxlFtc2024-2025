package org.firstinspires.ftc.teamcode.structure.controllers.lift;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.structure.controllers.LiftController;

@Config
public class LinearFunctionLiftController extends LiftController {
	private double calibrateVal;
	public static double vA;

	static {
		vA=0.05;
	}

	public LinearFunctionLiftController(@NonNull final DcMotorEx target) {
		super(target);
	}

	@Override
	public void modify() {
		calibrateVal=getErrorPosition()*vA;
	}

	@Override
	public double getCalibrateVal() {
		return calibrateVal;
	}
}
