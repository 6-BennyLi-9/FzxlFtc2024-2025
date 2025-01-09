package org.betastudio.ftc.controllers.lift;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.betastudio.ftc.controllers.LiftCtrl;

/**
 * 基于二次函数 {@code QuadrantFunction}
 */
@Config
@Disabled
public class QuadFuncLiftCtrl extends LiftCtrl {
	private       double calibrateVal;
	public static double vA;

	static {
		vA = 0.02;
	}

	public QuadFuncLiftCtrl(@NonNull final DcMotorEx target) {
		super(target);
	}

	@Override
	public void modify() {
		calibrateVal = getErrorPosition() * vA * (1 + getErrorPosition());
	}

	@Override
	public double getCalibrateVal() {
		return calibrateVal;
	}
}
