package org.firstinspires.ftc.teamcode.cores.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.util.Labeler;
import org.firstinspires.ftc.teamcode.cores.structure.positions.PlacePositions;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.controllers.ServoCtrl;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

public class PlaceOp implements Interfaces.HardwareController, Interfaces.InitializeRequested, Interfaces.TagOptionsRequired {
	public static final int PLACE_DECANT = 1;
	public static final int PLACE_IDLE = 0;
	public static final double PLACE_PREPARE = 0.5;
	public static final double PLACE_SAFE = 0.28;
	public static PlacePositions recent = PlacePositions.IDLE;
	public static  ServoCtrl      placeController;
	private static PlaceOp        instance;

	public static PlaceOp getInstance() {
		return instance;
	}

	@Override
	public void connect() {
		placeController = new ServoCtrl(HardwareDatabase.place, 0);

		placeController.setTag(Labeler.gen().summon(placeController));
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
		placeController.setTargetPosition(PLACE_DECANT);
	}

	public void idle() {
		recent = PlacePositions.IDLE;
		placeController.setTargetPosition(PLACE_IDLE);
	}

	public void prepare() {
		recent = PlacePositions.PREPARE;
		placeController.setTargetPosition(PLACE_PREPARE);
	}

	public void safe() {
		placeController.setTargetPosition(PLACE_SAFE);
	}

	@NonNull
	public Action initController() {
		connect();
		final Action res = getController();
		init();
		return res;
	}

	public void flip() {
		if (PlacePositions.DECANT == Objects.requireNonNull(recent)) {
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
	public void setTag(final String tag) {
		placeController.setTag("tag");
	}
}
