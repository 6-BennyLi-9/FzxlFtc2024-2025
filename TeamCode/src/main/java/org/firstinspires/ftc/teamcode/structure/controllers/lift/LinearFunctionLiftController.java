package org.firstinspires.ftc.teamcode.structure.controllers.lift;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.HardwareConstants;
import org.firstinspires.ftc.teamcode.structure.controllers.LiftController;

@Config
public class LinearFunctionLiftController extends LiftController {
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
