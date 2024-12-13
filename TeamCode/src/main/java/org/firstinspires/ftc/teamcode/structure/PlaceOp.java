package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoCtrl;
import org.firstinspires.ftc.teamcode.structure.positions.PlacePositionTypes;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.util.interfaces.HardwareController;
import org.firstinspires.ftc.teamcode.util.interfaces.InitializeRequested;
import org.firstinspires.ftc.teamcode.util.interfaces.TagRequested;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

public class PlaceOp implements HardwareController, InitializeRequested , TagRequested {
	public static  PlacePositionTypes recent = PlacePositionTypes.unknown;
	private static PlaceOp            instance;

	public static PlaceOp getInstance(){
		return instance;
	}

	public static ServoCtrl          placeController;

	@Override
	public void connect() {
		placeController = new ServoCtrl(HardwareConstants.place, 0);

		placeController.setTag("place");
	}

	@NonNull
	@Contract(" -> new")
	@Override
	public Action getController() {
		return placeController;
	}

	@Override
	public void writeToInstance() {
		instance=this;
	}

	@Override
	public void init() {
		idle();
	}

	public boolean decanting() {
		return PlacePositionTypes.decant == recent || PlacePositionTypes.prepare == recent;
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

	@Override
	public void setTag(String tag) {
		placeController.setTag("tag");
	}

	@Override
	public String getTag() {
		return placeController.getTag();
	}
}
