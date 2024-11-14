package org.firstinspires.ftc.teamcode.structure.controllers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.actions.Action;

public class ServoController implements Action {
	public final Servo controlTarget;
	private double targetPosition;
	private String tag;

	public ServoController(@NonNull final Servo target){
		this(target,0.5);
	}
	public ServoController(@NonNull final Servo target, final double defaultPosition){
		targetPosition=defaultPosition;
		controlTarget=target;
		tag=target.getDeviceName();
	}

	@Override
	public boolean run() {
		controlTarget.setPosition(targetPosition);
		return true;
	}

	@Override
	public String paramsString() {
		return tag+":"+targetPosition;
	}

	public void setTargetPosition(final double targetPosition) {
		this.targetPosition = targetPosition;
	}

	public void setTag(final String tag) {
		this.tag = tag;
	}
}
