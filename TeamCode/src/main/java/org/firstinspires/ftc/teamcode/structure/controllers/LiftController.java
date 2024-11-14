package org.firstinspires.ftc.teamcode.structure.controllers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.actions.Action;

public class LiftController implements Action {
	private long currentPosition,targetPosition;
	public final DcMotorEx targetLift;
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
			targetLift.setPower(10 >= currentPosition ? 0 : - 0.5);
			return true;
		}

		if(40 > Math.abs(delta)){
			targetLift.setPower(0);
		}else if(120 > Math.abs(delta)){
			targetLift.setPower(0.3 * Math.signum(delta));
		}else{
			targetLift.setPower(0.7 * Math.signum(delta));
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
