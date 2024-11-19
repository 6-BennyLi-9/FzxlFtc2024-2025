package org.firstinspires.ftc.teamcode.structure.controllers.lift;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.structure.controllers.LiftController;

@Config
public class ClassicLiftController extends LiftController {
	public static long     zeroPoseTargetingAllowError,staticAllowError,lowerErrorRange;
	public static double   zeroPoseCalibrationPow,lowerCalibrationPow,higherCalibrationPow;
	private double calibrateVal;

	static {
		zeroPoseTargetingAllowError=10;
		staticAllowError=40;
		lowerErrorRange=40;

		zeroPoseCalibrationPow=-0.5;
		lowerCalibrationPow=0.3;
		higherCalibrationPow=0.7;
	}

	public ClassicLiftController(@NonNull final DcMotorEx target) {
		super(target);
	}

	@Override
	public void modify() {
		if(0 == getTargetPosition()){
			calibrateVal= zeroPoseTargetingAllowError >= getCurrentPosition() ? 0 : - zeroPoseCalibrationPow;
			return;
		}

		if(staticAllowError > Math.abs(getErrorPosition())){
			calibrateVal= 0;
		}else if(lowerErrorRange > Math.abs(getErrorPosition())){
			calibrateVal= lowerCalibrationPow * Math.signum(getErrorPosition());
		}else{
			calibrateVal= higherCalibrationPow * Math.signum(getErrorPosition());
		}
	}

	@Override
	public double getCalibrateVal() {
		return calibrateVal;
	}
}
