package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;

public class ServoSmoothController {
	private final Servo  servo;
	private 	  double target, current;
	private double SMOOTH_VAL = 0.5;

	public ServoSmoothController(@NonNull Servo servo) {
		this.servo = servo;
		target = servo.getPosition();
		current = servo.getPosition();
	}

	public void setTarget(double target) {
		this.target = target;
	}

	public double getTarget() {
		return target;
	}

	public double getCurrent() {
		return current;
	}

	public void resolveCurrent(){
		current=target+(target-current) * SMOOTH_VAL;
	}

	public void updateSignal(){
		servo.setPosition(current);
	}

	@Deprecated
	public void set_SMOOTH_VAL(double SMOOTH_VAL) {
		this.SMOOTH_VAL = SMOOTH_VAL;
	}

	public void update(){
		resolveCurrent();
		updateSignal();
	}
}
