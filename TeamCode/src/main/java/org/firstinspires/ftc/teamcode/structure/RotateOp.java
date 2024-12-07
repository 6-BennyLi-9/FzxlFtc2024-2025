package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoCtrl;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.jetbrains.annotations.Contract;

public enum RotateOp {
	;
	public static ServoCtrl rotateController;

	public static void connect() {
		rotateController = new ServoCtrl(HardwareConstants.rotate, 0.79);

		rotateController.setTag("rotate");
	}

	public static void init() {
		mid();
	}

	public static void mid() {
		rotateController.setTargetPosition(0.79);
	}

	//	public static void turnLeft(){
//		rotateController.changeTargetPositionBy(0.1);
//	}
//	public static void turnRight(){
//		rotateController.changeTargetPositionBy(-0.1);
//	}
	public static void turn(final double position) {
		rotateController.changeTargetPositionBy(position);
	}

	@NonNull
	@Contract(" -> new")
	public static Action getController() {
		return rotateController;
	}

	@NonNull
	public static Action initController() {
		connect();
		final Action res = getController();
		init();
		return res;
	}
}
