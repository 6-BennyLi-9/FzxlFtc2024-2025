package org.firstinspires.ftc.teamcode.structure.controllers.lift;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.structure.controllers.LiftCtrl;

/**
 * 基于一次函数 {@code LinearFunction}
 */
@Config
public class LinFuncLiftCtrl extends LiftCtrl {
	private double calibrateVal;
	public static double vA;

	static {
		vA=0.05;
	}

	public LinFuncLiftCtrl(@NonNull final DcMotorEx target) {
		super(target);
	}

	@Override
	public void modify() {
		if(0 == getTargetPosition()){
			if(HardwareConstants.liftTouch.isPressed()){//没到
				calibrateVal=-1;
			}else{
				calibrateVal=0;
			}
			return;
		}
		calibrateVal=getErrorPosition()*vA;
	}

	@Override
	public double getCalibrateVal() {
		return calibrateVal;
	}
}
