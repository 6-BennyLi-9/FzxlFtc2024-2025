package org.firstinspires.ftc.teamcode.structure.controllers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.actions.Action;
import org.firstinspires.ftc.teamcode.structure.LiftOption;

/**
 * @see LiftOption 在这里修改参数
 */
public class LiftController implements Action {
	private long currentPosition,targetPosition;
	public final DcMotorEx targetLift;

	public long     zeroPoseTargetingAllowError = 10,staticAllowError = 50,lowerErrorRange = 100;
	public double   zeroPoseCalibrationPow = 0.5,lowerCalibrationPow = 0.3,higherCalibrationPow = 0.7;

	private String tag;

	public LiftController(@NonNull final DcMotorEx target){
		targetLift=target;
		tag=target.getDeviceName();
	}


	@Override
	public boolean run() {
		currentPosition= targetLift	.getCurrentPosition();
		final long delta=targetPosition- currentPosition;

		if(0 == targetPosition){
			targetLift.setPower(zeroPoseTargetingAllowError >= currentPosition ? 0 : - zeroPoseCalibrationPow);
			return true;
		}

		if(staticAllowError > Math.abs(delta)){
			targetLift.setPower(0);
		}else if(lowerErrorRange > Math.abs(delta)){
			targetLift.setPower(lowerCalibrationPow * Math.signum(delta));
		}else{
			targetLift.setPower(higherCalibrationPow * Math.signum(delta));
		}
		return true;
	}
	@Override
	public String paramsString() {
		return tag+":"+currentPosition+"->"+targetPosition;
	}

	public void setTargetPosition(final long targetPosition) {
		this.targetPosition = targetPosition;
	}

	public void setTag(final String tag) {
		this.tag = tag;
	}
}
