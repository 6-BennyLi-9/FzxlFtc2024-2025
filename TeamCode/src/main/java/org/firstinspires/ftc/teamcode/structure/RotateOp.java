package org.firstinspires.ftc.teamcode.structure;

import static org.betastudio.ftc.Interfaces.HardwareController;
import static org.betastudio.ftc.Interfaces.InitializeRequested;
import static org.betastudio.ftc.Interfaces.TagOptionsRequired;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.ROTATE_DEFAULT;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.util.Labeler;
import org.firstinspires.ftc.teamcode.Hardwares;
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
		rotateController = new ServoCtrl(Hardwares.rotate, ROTATE_DEFAULT);

		rotateController.setTag(Labeler.summon(rotateController));
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
		rotateController.setTargetPosition(ROTATE_DEFAULT);
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
