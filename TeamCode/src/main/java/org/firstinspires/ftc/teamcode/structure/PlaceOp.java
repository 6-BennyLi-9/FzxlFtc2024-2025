package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoCtrl;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

public enum PlaceOp {
	;
	public enum PlacePositionTypes {
		idle, decant,
		prepare,
		unknown
	}

	public static PlacePositionTypes recent = PlacePositionTypes.unknown;

	public static ServoCtrl          placeController;
	public static void connect() {
		placeController = new ServoCtrl(HardwareConstants.place, 0);

		placeController.setTag("place");
	}

	public static boolean decanting() {
		return PlacePositionTypes.decant == recent || PlacePositionTypes.prepare == recent;
	}

	public static void init() {
		idle();
	}

	public static void decant() {
		recent = PlacePositionTypes.decant;
		placeController.setTargetPosition(1);
	}

	public static void idle() {
		recent = PlacePositionTypes.idle;
		placeController.setTargetPosition(0);
	}

	public static void prepare(){
		recent = PlacePositionTypes.prepare;
		placeController.setTargetPosition(0.5);
	}

	public static void safe() {
		placeController.setTargetPosition(0.28);
	}

	@NonNull
	@Contract(" -> new")
	public static Action getController() {
		return placeController;
	}

	@NonNull
	public static Action initController() {
		connect();
		final Action res = getController();
		init();
		return res;
	}

	public static void flip() {
		if (Objects.requireNonNull(recent) == PlacePositionTypes.idle) {
			decant();
		} else {
			idle();
		}
	}
}
