package org.firstinspires.ftc.teamcode.structure.controllers.lift;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.structure.controllers.LiftCtrl;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;

/**
 * 使用 {@link DcMotorEx} 为基础的控制器
 */
public class DcLiftCtrl extends LiftCtrl {
	public static double bufPow=1;
	public static int tolerance=10;

	public DcLiftCtrl(@NonNull final DcMotorEx target) {
		super(target);
	}

	@Override
	public boolean run() {
		if(0 == getTargetPosition()){
			targetLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
			targetLift.setPower(! HardwareConstants.liftTouch.isPressed() ? 0 : - 1);
			if(!HardwareConstants.liftTouch.isPressed()){
				targetLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			}
			return true;
		}

		targetLift.setTargetPosition((int) getTargetPosition());
		targetLift.setTargetPositionTolerance(tolerance);
		targetLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		targetLift.setPower(bufPow);

		return true;
	}

	@Override public void modify() {}
	@Override public double getCalibrateVal() {return 0;}
}
