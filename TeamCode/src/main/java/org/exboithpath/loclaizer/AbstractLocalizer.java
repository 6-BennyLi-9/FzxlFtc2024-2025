package org.exboithpath.loclaizer;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public abstract class AbstractLocalizer implements Localizer {
	///Encoders
	protected DcMotorEx left,middle,right;

	protected AbstractLocalizer(@NonNull HardwareMap hardwareMap){
		left = hardwareMap.get(DcMotorEx.class,"rightFront");
		middle = hardwareMap.get(DcMotorEx.class,"leftRear");
		right = hardwareMap.get(DcMotorEx.class,"leftFront");

		middle.setDirection(DcMotorSimple.Direction.REVERSE);
		right.setDirection(DcMotorSimple.Direction.REVERSE);
	}
}
