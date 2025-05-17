package org.firstinspires.ftc.teamcode.structure;

import static org.betastudio.ftc.Interfaces.HardwareController;
import static org.betastudio.ftc.Interfaces.InitializeRequested;
import static org.betastudio.ftc.Interfaces.TagOptionsRequired;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.PLACE_DECANT;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.PLACE_IDLE;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.PLACE_PREPARE;
import static org.firstinspires.ftc.teamcode.structure.HardwareSituation.PlacePositions;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.util.Labeler;
import org.firstinspires.ftc.teamcode.Hardwares;
import org.firstinspires.ftc.teamcode.controllers.ServoCtrl;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

public class PlaceOp implements HardwareController, InitializeRequested, TagOptionsRequired {
	public static  PlacePositions recent = PlacePositions.IDLE;
	public static  ServoCtrl      placeController;
	private static PlaceOp        instance;

	public static PlaceOp getInstance() {
		return instance;
	}

	@Override
	public void connect() {
		placeController = new ServoCtrl(Hardwares.place, 0);

		placeController.setTag(Labeler.summon(placeController));
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
