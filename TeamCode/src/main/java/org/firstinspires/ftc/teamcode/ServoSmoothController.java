package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;

public class ServoSmoothController {
	private final Servo  servo;
	private       double target;
	private       double current;
	private       double SMOOTH_VAL = 0.45;

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

	public strictfp void resolveCurrent(){
		current = target + (target - current) * SMOOTH_VAL;
	}

	public strictfp void updateSignal(){
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
