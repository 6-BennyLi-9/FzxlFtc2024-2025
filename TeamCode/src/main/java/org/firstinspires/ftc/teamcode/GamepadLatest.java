package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.client.TelemetryClient;

public enum GamepadLatest {
	a,b,x,y,dpadUp,dpadDown,dpadLeft,dpadRight,leftBumper,rightBumper;
	public boolean value;

	public static void sync(@NonNull final Gamepad gamepad){
		a.value=gamepad.a;
		b.value= gamepad.b;
		x.value=gamepad.x;
		y.value=gamepad.y;

		dpadUp.value=gamepad.dpad_up;
		dpadDown.value=gamepad.dpad_down;
		dpadLeft.value=gamepad.dpad_left;
		dpadRight.value=gamepad.dpad_right;

		leftBumper.value=gamepad.left_bumper;
		rightBumper.value=gamepad.right_bumper;
	}

	public static void output(){
		for(final GamepadLatest value:values()){
			TelemetryClient.getInstance().changeData("gamepad "+value.name(),value.value);
		}
	}
}
