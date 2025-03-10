package org.firstinspires.ftc.teamcode.cores.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.util.Labeler;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.controllers.ServoCtrl;
import org.jetbrains.annotations.Contract;

public class RotateOp implements Interfaces.HardwareController, Interfaces.InitializeRequested, Interfaces.TagOptionsRequired {
	public static  ServoCtrl rotateController;
	private static RotateOp  instance;

	public static RotateOp getInstance() {
		return instance;
	}

	@Override
	public void connect() {
		rotateController = new ServoCtrl(HardwareDatabase.rotate, 0.79);

		rotateController.setTag(Labeler.gen().summon(rotateController));
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
	public void setTag(final String tag) {
		rotateController.setTag(tag);
	}
}
