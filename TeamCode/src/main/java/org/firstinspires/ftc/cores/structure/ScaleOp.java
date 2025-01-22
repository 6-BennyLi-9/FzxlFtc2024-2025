package org.firstinspires.ftc.cores.structure;

import static java.lang.Math.*;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.utils.ThreadedAction;
import org.betastudio.ftc.util.HardwareController;
import org.betastudio.ftc.util.InitializeRequested;
import org.betastudio.ftc.util.TagOptionsRequired;
import org.firstinspires.ftc.cores.structure.positions.ScalePositions;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.Labeler;
import org.firstinspires.ftc.teamcode.controllers.ServoCtrl;
import org.jetbrains.annotations.Contract;

public class ScaleOp implements HardwareController, InitializeRequested, TagOptionsRequired {
	public static ScalePositions recent = ScalePositions.BACK;
	public static ServoCtrl      leftScaleController, rightScaleController;
	public static double smooth = 0.2;
	private static ScaleOp instance;

	public static ScaleOp getInstance() {
		return instance;
	}

	@Override
	public void connect() {
		leftScaleController = new ServoCtrl(HardwareDatabase.leftScale, 1);
		rightScaleController = new ServoCtrl(HardwareDatabase.rightScale, 0.5);

		leftScaleController.setTag(Labeler.generate().summonID(leftScaleController));
		rightScaleController.setTag(Labeler.generate().summonID(rightScaleController));
	}

	@NonNull
	@Contract(" -> new")
	@Override
	public Action getController() {
		return new ThreadedAction(leftScaleController, rightScaleController);
	}

	@Override
	public void writeToInstance() {
		instance = this;
	}

	public void manage(double position) {
		position = min(max(position, 0),0.35);
		leftScaleController.setTargetPosition(1 - position);
		rightScaleController.setTargetPosition(position);
	}

	public void manageSmooth(double position) {
		position = min(max(position, 0),0.35);
		leftScaleController.setTargetPositionTolerance(1 - position, smooth);
		rightScaleController.setTargetPositionTolerance(position, smooth);
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
		manage(0.35);
	}

	public void back() {
		recent = ScalePositions.BACK;
		manage(0);
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
