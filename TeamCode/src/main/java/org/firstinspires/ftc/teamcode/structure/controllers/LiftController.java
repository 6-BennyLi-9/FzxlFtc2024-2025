package org.firstinspires.ftc.teamcode.structure.controllers;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.actions.Action;

public class LiftController implements Action {
	private long currentPosition,targetPosition;
	public final DcMotorEx targetLift;

	public LiftController(final DcMotorEx target){
		targetLift=target;
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
		return targetLift.getDeviceName()+":"+currentPosition+"->"+targetPosition;
	}
}
