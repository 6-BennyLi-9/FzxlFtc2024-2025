package org.firstinspires.ftc.teamcode.cores.structure;

import androidx.annotation.*;
import org.betastudio.ftc.*;
import org.betastudio.ftc.action.*;
import org.betastudio.ftc.action.utils.*;
import org.betastudio.ftc.util.*;
import org.firstinspires.ftc.teamcode.*;
import org.firstinspires.ftc.teamcode.controllers.*;
import org.firstinspires.ftc.teamcode.cores.structure.positions.*;
import org.jetbrains.annotations.*;

import static java.lang.Math.*;

public class ScaleOp implements Interfaces.HardwareController, Interfaces.InitializeRequested, Interfaces.TagOptionsRequired {
	public static final double         SMOOTH = 0.2;
	public static final double SCALE_PROBE = 0.35;
	public static final int SCALE_BACH = 0;
	public static       ScalePositions recent = ScalePositions.BACK;
	public static       ServoCtrl      leftScaleController;
	public static       ServoCtrl      rightScaleController;
	private static      ScaleOp        instance;

	public static ScaleOp getInstance() {
		return instance;
	}

	@Override
	public void connect() {
		leftScaleController = new ServoCtrl(HardwareDatabase.leftScale, 1);
		rightScaleController = new ServoCtrl(HardwareDatabase.rightScale, 0.5);

		leftScaleController.setTag(Labeler.gen().summon(leftScaleController));
		rightScaleController.setTag(Labeler.gen().summon(rightScaleController));
	}

	@NonNull
	@Contract(" -> new")
	@Override
	public Action getController() {
		return new AssembledAction(leftScaleController, rightScaleController);
	}

	@Override
	public void writeToInstance() {
		instance = this;
	}

	public void manage(double position) {
		position = min(max(position, HardwareConfigures.SCALE_MIN_POSITION), HardwareConfigures.SCALE_MAX_POSITION);
		leftScaleController.setTargetPosition(1 - position);
		rightScaleController.setTargetPosition(position);
	}

	public void manageSmooth(double position) {
		position = min(max(position, HardwareConfigures.SCALE_MIN_POSITION), HardwareConfigures.SCALE_MAX_POSITION);
		leftScaleController.setTargetPositionTolerance(1 - position, SMOOTH);
		rightScaleController.setTargetPositionTolerance(position, SMOOTH);
	}

	@Override
	public void init() {
		back();
	}

	public void flip() {
		if (ScalePositions.PROBE == recent) {
			back();
		} else {
			probe();
		}
	}

	public void probe() {
		recent = ScalePositions.PROBE;
		manage(SCALE_PROBE);
	}

	public void back() {
		recent = ScalePositions.BACK;
		manage(SCALE_BACH);
	}

	public void operate(final double position) {
		recent = ScalePositions.PROBE;
		manageSmooth(position);
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
		throw new IllegalStateException("CANNOT GET TAG OF MULTI TAGS");
	}

	@Override
	public void setTag(final String tag) {
		leftScaleController.setTag("left " + tag);
		rightScaleController.setTag("right " + tag);
	}
}
