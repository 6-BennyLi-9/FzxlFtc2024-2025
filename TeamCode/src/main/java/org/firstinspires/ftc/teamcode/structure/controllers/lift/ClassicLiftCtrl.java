package org.firstinspires.ftc.teamcode.structure.controllers.lift;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.structure.controllers.LiftCtrl;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;

/**
 * 经典的电梯控制，人走码还在
 */
@Config
@Disabled
public class ClassicLiftCtrl extends LiftCtrl {
	public static long   staticAllowError;
	public static long   lowerErrorRange;
	public static double zeroPoseCalibrationPow, lowerCalibrationPow, higherCalibrationPow;
	private double calibrateVal;

	static {
		staticAllowError = 35;
		lowerErrorRange = 60;

		zeroPoseCalibrationPow = 1;
		lowerCalibrationPow = 0.35;
		higherCalibrationPow = 1;
	}

	public ClassicLiftCtrl(@NonNull final DcMotorEx target) {
		super(target);
	}

	@Override
	public void modify() {
		if (0 == getTargetPosition()) {
			calibrateVal = ! HardwareConstants.liftTouch.isPressed() ? 0 : - zeroPoseCalibrationPow;
			return;
		}

		if (staticAllowError > Math.abs(getErrorPosition())) {
			calibrateVal = 0;
		} else if (lowerErrorRange > Math.abs(getErrorPosition())) {
			calibrateVal = lowerCalibrationPow * Math.signum(getErrorPosition());
		} else {
			calibrateVal = higherCalibrationPow * Math.signum(getErrorPosition());
		}
	}

	@Override
	public double getCalibrateVal() {
		return calibrateVal;
	}
}
