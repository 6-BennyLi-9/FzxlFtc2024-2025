package org.firstinspires.ftc.teamcore.autonomous.structure;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.betastudio.ftc.controllers.LiftCtrl;
import org.firstinspires.ftc.teamcode.HardwareDatabase;

@Config
@Disabled
public class ClassicLiftCtrl extends LiftCtrl {
	public static long   staticAllowError;
	public static long   lowerErrorRange;
	public static double zeroPoseCalibrationPow, lowerCalibrationPow, higherCalibrationPow;
	private double  calibrateVal;
	private boolean calibrationDone;

	static {
		staticAllowError = 20;
		lowerErrorRange = 50;

		zeroPoseCalibrationPow = 1;
		lowerCalibrationPow = 0.3;
		higherCalibrationPow = 0.7;
	}

	public ClassicLiftCtrl(@NonNull final DcMotorEx target, final long targetPosition) {
		super(target);
		setTargetPosition(targetPosition);
		disableInfinityRun();
	}

	@Override
	public void modify() {
		if (0 == getTargetPosition()) {
			calibrateVal = ! HardwareDatabase.liftTouch.isPressed() ? 0 : - zeroPoseCalibrationPow;
			calibrationDone = 0 == calibrateVal;
			return;
		}

		if (staticAllowError > Math.abs(getErrorPosition())) {
			calibrateVal = 0;
			calibrationDone = true;
		} else if (lowerErrorRange > Math.abs(getErrorPosition())) {
			calibrateVal = lowerCalibrationPow * Math.signum(getErrorPosition());
			calibrationDone = false;
		} else {
			calibrateVal = higherCalibrationPow * Math.signum(getErrorPosition());
			calibrationDone = false;
		}
	}

	@Override
	public double getCalibrateVal() {
		return calibrateVal;
	}

	@Override
	public boolean getCalibrateDone() {
		return calibrationDone;
	}
}
