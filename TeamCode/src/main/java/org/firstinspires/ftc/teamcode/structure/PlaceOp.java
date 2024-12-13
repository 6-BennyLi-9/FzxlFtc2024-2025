package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoCtrl;
import org.firstinspires.ftc.teamcode.structure.positions.PlacePositionTypes;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.util.implement.HardwareController;
import org.firstinspires.ftc.teamcode.util.implement.InitializeRequested;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

public class PlaceOp implements HardwareController, InitializeRequested {
	private static PlaceOp instance;
	public static PlacePositionTypes recent = PlacePositionTypes.unknown;

	public static PlaceOp getInstance(){
		return instance;
	}

	public static ServoCtrl          placeController;

	@Override
	public void connect() {
		placeController = new ServoCtrl(HardwareConstants.place, 0);

		placeController.setTag("place");
	}

	public boolean decanting() {
		return PlacePositionTypes.decant == recent || PlacePositionTypes.prepare == recent;
	}

	@Override
	public void init() {
		idle();
	}

	public void decant() {
		recent = PlacePositionTypes.decant;
		placeController.setTargetPosition(1);
	}

	public void idle() {
		recent = PlacePositionTypes.idle;
		placeController.setTargetPosition(0);
	}

	public void prepare(){
		recent = PlacePositionTypes.prepare;
		placeController.setTargetPosition(0.5);
	}

	public void safe() {
		placeController.setTargetPosition(0.28);
	}

	@NonNull
	@Contract(" -> new")
	public Action getController() {
		return placeController;
	}

	@NonNull
	public Action initController() {
		connect();
		final Action res = getController();
		init();
		return res;
	}

	public void flip() {
		if (Objects.requireNonNull(recent) == PlacePositionTypes.idle) {
			decant();
		} else {
			idle();
		}
	}
}
