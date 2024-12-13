package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.utils.ThreadedAction;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoCtrl;
import org.firstinspires.ftc.teamcode.structure.positions.ScalePositionTypes;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.util.interfaces.HardwareController;
import org.firstinspires.ftc.teamcode.util.interfaces.InitializeRequested;
import org.firstinspires.ftc.teamcode.util.interfaces.TagRequested;
import org.jetbrains.annotations.Contract;

public class ScaleOp implements HardwareController, InitializeRequested , TagRequested {
	private static ScaleOp instance;
	public static ScalePositionTypes recent = ScalePositionTypes.unknown;
	public static ServoCtrl          leftScaleController, rightScaleController;

	public static ScaleOp getInstance(){
		return instance;
	}

	@Override
	public void connect() {
		leftScaleController = new ServoCtrl(HardwareConstants.leftScale, 1);
		rightScaleController = new ServoCtrl(HardwareConstants.rightScale, 0.5);

		leftScaleController.setTag("leftScale");
		rightScaleController.setTag("rightScale");
	}

	@NonNull
	@Contract(" -> new")
	@Override
	public Action getController() {
		return new ThreadedAction(leftScaleController, rightScaleController);
	}

	public static double smooth = 0.2;

	public void manage(double position) {
		position = Math.max(position, 0.5);
		leftScaleController.setTargetPosition(1.5 - position);
		rightScaleController.setTargetPosition(position);
	}

	public void manageSmooth(double position) {
		position = Math.min(Math.max(position, 0.5), 1);
		leftScaleController.setTargetPositionTolerance(1.5 - position, smooth);
		rightScaleController.setTargetPositionTolerance(position, smooth);
	}

	@Override
	public void init() {
		back();
	}

	public void flip() {
		if (ScalePositionTypes.probe == recent) {
			back();
		} else {
			probe();
		}
	}

	public void probe() {
		recent = ScalePositionTypes.probe;
		manage(1);
	}

	public void back() {
		recent = ScalePositionTypes.back;
		manage(0.5);
	}

	public void operate(final double position) {
		recent = ScalePositionTypes.probe;
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
	public void setTag(String tag) {
		leftScaleController.setTag("left "+tag);
		rightScaleController.setTag("right "+tag);
	}

	@Override
	public String getTag() {
		throw new IllegalStateException("CANNOT GET TAG OF MULTI TAGS");
	}
}
