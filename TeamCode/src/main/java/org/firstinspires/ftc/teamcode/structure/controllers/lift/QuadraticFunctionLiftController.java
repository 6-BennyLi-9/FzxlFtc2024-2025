package org.firstinspires.ftc.teamcode.structure.controllers.lift;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.structure.controllers.LiftController;

@Config
public class QuadraticFunctionLiftController extends LiftController {
	private double calibrateVal;
	public static double vA;

	static {
		vA=0.02;
	}

	public QuadraticFunctionLiftController(@NonNull final DcMotorEx target) {
		super(target);
	}

	@Override
	public void modify() {
		calibrateVal=getErrorPosition()*vA * (1+getErrorPosition());
	}

	@Override
	public double getCalibrateVal() {
		return calibrateVal;
	}
}
