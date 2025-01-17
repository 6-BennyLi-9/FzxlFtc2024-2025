package org.firstinspires.ftc.cores.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.interfaces.HardwareController;
import org.betastudio.ftc.interfaces.InitializeRequested;
import org.betastudio.ftc.interfaces.TagOptionsRequired;
import org.firstinspires.ftc.cores.structure.positions.PlacePositions;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.Labeler;
import org.firstinspires.ftc.teamcode.controllers.ServoCtrl;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

public class PlaceOp implements HardwareController, InitializeRequested, TagOptionsRequired {
	public static  PlacePositions recent = PlacePositions.IDLE;
	public static ServoCtrl placeController;
	private static PlaceOp        instance;

	public static PlaceOp getInstance() {
		return instance;
	}

	public static void setInstance(PlaceOp instance) {
		PlaceOp.instance = instance;
	}

	@Override
	public void connect() {
		placeController = new ServoCtrl(HardwareDatabase.place, 0);

		placeController.setTag(Labeler.generate().summonID(placeController));
	}

	@NonNull
	@Contract(" -> new")
	@Override
	public Action getController() {
		return placeController;
	}

	@Override
	public void writeToInstance() {
		instance = this;
	}

	@Override
	public void init() {
		idle();
	}

	public boolean decanting() {
		return PlacePositions.DECANT == recent || PlacePositions.PREPARE == recent;
	}

	public void decant() {
		recent = PlacePositions.DECANT;
		placeController.setTargetPosition(1);
	}

	public void idle() {
		recent = PlacePositions.IDLE;
		placeController.setTargetPosition(0);
	}

	public void prepare() {
		recent = PlacePositions.PREPARE;
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
		if (Objects.requireNonNull(recent) == PlacePositions.DECANT) {
			idle();
		} else {
			decant();
		}
	}

	@Override
	public String getTag() {
		return placeController.getTag();
	}

	@Override
	public void setTag(String tag) {
		placeController.setTag("tag");
	}
}
