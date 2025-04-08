package org.firstinspires.ftc.teamcode.cores.structure;

import static java.lang.Math.max;
import static java.lang.Math.min;

import androidx.annotation.NonNull;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.utils.AssembledAction;
import org.betastudio.ftc.util.Labeler;
import org.firstinspires.ftc.teamcode.HardwareConfigures;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.controllers.ServoCtrl;
import org.firstinspires.ftc.teamcode.cores.structure.positions.ScalePositions;
import org.jetbrains.annotations.Contract;

public class ScaleOp implements Interfaces.HardwareController, Interfaces.InitializeRequested, Interfaces.TagOptionsRequired {
	public static final double         SMOOTH = 0.2;
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
		manage(HardwareConfigures.SCALE_PROBE);
	}

	public void back() {
		recent = ScalePositions.BACK;
		manage(HardwareConfigures.SCALE_BACH);
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
