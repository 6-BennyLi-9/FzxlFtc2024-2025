package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoCtrl;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.util.implement.HardwareController;
import org.firstinspires.ftc.teamcode.util.implement.InitializeRequested;
import org.firstinspires.ftc.teamcode.util.implement.TagRequested;
import org.jetbrains.annotations.Contract;

public class RotateOp implements HardwareController, InitializeRequested , TagRequested {
	private static RotateOp instance;
	public static ServoCtrl rotateController;

	public static RotateOp getInstance(){
		return instance;
	}

	@Override
	public void connect() {
		rotateController = new ServoCtrl(HardwareConstants.rotate, 0.79);

		rotateController.setTag("rotate");
	}

	@Override
	public void init() {
		mid();
	}

	@NonNull
	@Contract(" -> new")
	@Override
	public Action getController() {
		return rotateController;
	}

	public void mid() {
		rotateController.setTargetPosition(0.79);
	}

	public void turn(final double position) {
		rotateController.changeTargetPositionBy(position);
	}

	@NonNull
	public Action initController() {
		connect();
		final Action res = getController();
		init();
		return res;
	}

	@Override
	public void setTag(String tag) {
		rotateController.setTag(tag);
	}

	@Override
	public String getTag() {
		return rotateController.getTag();
	}
}
