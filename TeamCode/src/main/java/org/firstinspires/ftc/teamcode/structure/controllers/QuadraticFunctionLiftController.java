package org.firstinspires.ftc.teamcode.structure.controllers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class QuadraticFunctionLiftController extends LiftController{
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
