package org.firstinspires.ftc.teamcode.structure.controllers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class LinearFunctionLiftController extends LiftController{
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
