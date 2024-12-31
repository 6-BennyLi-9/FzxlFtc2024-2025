package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoCtrl;
import org.firstinspires.ftc.teamcode.util.HardwareDatabase;
import org.firstinspires.ftc.teamcode.util.interfaces.HardwareController;
import org.firstinspires.ftc.teamcode.util.interfaces.InitializeRequested;
import org.firstinspires.ftc.teamcode.util.interfaces.TagRequested;
import org.jetbrains.annotations.Contract;

public class RotateOp implements HardwareController, InitializeRequested , TagRequested {
	public static  ServoCtrl rotateController;
	private static RotateOp  instance;

	public static RotateOp getInstance(){
		return instance;
	}

	@Override
	public void connect() {
		rotateController = new ServoCtrl(HardwareDatabase.rotate, 0.79);

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

	@Override
	public void writeToInstance() {
		instance=this;
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
