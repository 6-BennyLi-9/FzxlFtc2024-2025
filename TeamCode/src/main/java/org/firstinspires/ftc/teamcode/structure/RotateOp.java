package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoCtrl;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.util.implement.HardwareController;
import org.firstinspires.ftc.teamcode.util.implement.InitializeRequested;
import org.jetbrains.annotations.Contract;

public class RotateOp implements HardwareController, InitializeRequested {
	private static RotateOp instance;
	public static ServoCtrl rotateController;

	public static RotateOp getInstance(){
		return instance;
	}

	public void connect() {
		rotateController = new ServoCtrl(HardwareConstants.rotate, 0.79);

		rotateController.setTag("rotate");
	}

	public void init() {
		mid();
	}

	public void mid() {
		rotateController.setTargetPosition(0.79);
	}

	public void turn(final double position) {
		rotateController.changeTargetPositionBy(position);
	}

	@NonNull
	@Contract(" -> new")
	public Action getController() {
		return rotateController;
	}

	@NonNull
	public Action initController() {
		connect();
		final Action res = getController();
		init();
		return res;
	}
}
