package org.firstinspires.ftc.cores.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.interfaces.HardwareController;
import org.betastudio.ftc.interfaces.InitializeRequested;
import org.betastudio.ftc.interfaces.TagOptionsRequired;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.Labeler;
import org.firstinspires.ftc.teamcode.controllers.ServoCtrl;
import org.jetbrains.annotations.Contract;

public class RotateOp implements HardwareController, InitializeRequested, TagOptionsRequired {
	public static  ServoCtrl rotateController;
	private static RotateOp  instance;

	public static RotateOp getInstance() {
		return instance;
	}

	@Override
	public void connect() {
		rotateController = new ServoCtrl(HardwareDatabase.rotate, 0.79);

		rotateController.setTag(Labeler.generate().summonID(rotateController));
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
		instance = this;
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
	public String getTag() {
		return rotateController.getTag();
	}

	@Override
	public void setTag(String tag) {
		rotateController.setTag(tag);
	}
}
