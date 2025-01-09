package org.firstinspires.ftc.teamcore.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.controllers.ServoCtrl;
import org.firstinspires.ftc.teamcore.structure.positions.PlacePositions;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.interfaces.HardwareController;
import org.firstinspires.ftc.teamcode.interfaces.InitializeRequested;
import org.firstinspires.ftc.teamcode.interfaces.TagRequested;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

public class PlaceOp implements HardwareController, InitializeRequested , TagRequested {
	public static  PlacePositions recent = PlacePositions.idle;
	private static PlaceOp        instance;

	public static PlaceOp getInstance(){
		return instance;
	}

	public static ServoCtrl          placeController;

	@Override
	public void connect() {
		placeController = new ServoCtrl(HardwareDatabase.place, 0);

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
		return PlacePositions.decant == recent || PlacePositions.prepare == recent;
	}

	public void decant() {
		recent = PlacePositions.decant;
		placeController.setTargetPosition(1);
	}

	public void idle() {
		recent = PlacePositions.idle;
		placeController.setTargetPosition(0);
	}

	public void prepare(){
		recent = PlacePositions.prepare;
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
		if (Objects.requireNonNull(recent) == PlacePositions.decant) {
			idle();
		} else {
			decant();
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
