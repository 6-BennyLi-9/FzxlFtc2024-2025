package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.utils.ThreadedAction;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoCtrl;
import org.firstinspires.ftc.teamcode.structure.positions.ScalePositionTypes;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.jetbrains.annotations.Contract;

public class ScaleOp {

	public static ScalePositionTypes recent = ScalePositionTypes.unknown;
	public static ServoCtrl          leftScaleController, rightScaleController;

	public static void connect() {
		leftScaleController = new ServoCtrl(HardwareConstants.leftScale, 1);
		rightScaleController = new ServoCtrl(HardwareConstants.rightScale, 0.5);

		leftScaleController.setTag("leftScale");
		rightScaleController.setTag("rightScale");
	}

	public static double smooth = 0.2;

	public static void manage(double position) {
		position = Math.max(position, 0.5);
		leftScaleController.setTargetPosition(1.5 - position);
		rightScaleController.setTargetPosition(position);
	}

	public static void manageSmooth(double position) {
		position = Math.min(Math.max(position, 0.5), 1);
		leftScaleController.setTargetPositionTolerance(1.5 - position, smooth);
		rightScaleController.setTargetPositionTolerance(position, smooth);
	}

	public static void init() {
		back();
	}

	public static void flip() {
		if (ScalePositionTypes.probe == recent) {
			back();
		} else {
			probe();
		}
	}

	public static void probe() {
		recent = ScalePositionTypes.probe;
		manage(1);
	}

	public static void back() {
		recent = ScalePositionTypes.back;
		manage(0.5);
	}

	public static void operate(final double position) {
		recent = ScalePositionTypes.probe;
		manageSmooth(position);
	}

	@NonNull
	@Contract(" -> new")
	public static Action getController() {
		return new ThreadedAction(leftScaleController, rightScaleController);
	}

	@NonNull
	public static Action initController() {
		connect();
		final Action res = getController();
		init();
		return res;
	}
}
