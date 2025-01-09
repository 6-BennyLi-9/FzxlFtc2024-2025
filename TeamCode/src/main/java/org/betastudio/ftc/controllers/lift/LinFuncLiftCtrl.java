package org.betastudio.ftc.controllers.lift;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.betastudio.ftc.controllers.LiftCtrl;
import org.firstinspires.ftc.teamcode.util.HardwareDatabase;

/**
 * 基于一次函数 {@code LinearFunction}
 */
@Config
@Disabled
public class LinFuncLiftCtrl extends LiftCtrl {
	private       double calibrateVal;
	public static double vA;

	static {
		vA = 0.05;
	}

	public LinFuncLiftCtrl(@NonNull final DcMotorEx target) {
		super(target);
	}

	@Override
	public void modify() {
		if (0 == getTargetPosition()) {
			if (HardwareDatabase.liftTouch.isPressed()) {//没到
				calibrateVal = - 1;
			} else {
				calibrateVal = 0;
			}
			return;
		}
		calibrateVal = getErrorPosition() * vA;
	}

	@Override
	public double getCalibrateVal() {
		return calibrateVal;
	}
}
