package org.firstinspires.ftc.teamcode.structure.controllers;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.actions.Action;

public class ServoController implements Action {
	public final Servo controlTarget;
	private double targetPosition;

	public ServoController(final Servo target){
		this(target,0.5);
	}
	public ServoController(final Servo target, final double defaultPosition){
		targetPosition=defaultPosition;
		controlTarget=target;
	}

	@Override
	public boolean run() {
		controlTarget.setPosition(targetPosition);
		return true;
	}

	@Override
	public String paramsString() {
		return controlTarget.getDeviceName()+":"+targetPosition;
	}

	public void setTargetPosition(final double targetPosition) {
		this.targetPosition = targetPosition;
	}
}
