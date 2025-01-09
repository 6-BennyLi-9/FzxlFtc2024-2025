package org.firstinspires.ftc.teamcore.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.utils.ThreadedAction;
import org.betastudio.ftc.controllers.ServoCtrl;
import org.firstinspires.ftc.teamcore.structure.positions.ScalePositions;
import org.firstinspires.ftc.teamcode.util.HardwareDatabase;
import org.firstinspires.ftc.teamcode.util.interfaces.HardwareController;
import org.firstinspires.ftc.teamcode.util.interfaces.InitializeRequested;
import org.firstinspires.ftc.teamcode.util.interfaces.TagRequested;
import org.jetbrains.annotations.Contract;

public class ScaleOp implements HardwareController, InitializeRequested , TagRequested {
	public static ScalePositions recent = ScalePositions.back;
	public static ServoCtrl      leftScaleController, rightScaleController;
	private static ScaleOp            instance;

	public static ScaleOp getInstance(){
		return instance;
	}

	@Override
	public void connect() {
		leftScaleController = new ServoCtrl(HardwareDatabase.leftScale, 1);
		rightScaleController = new ServoCtrl(HardwareDatabase.rightScale, 0.5);

		leftScaleController.setTag("leftScale");
		rightScaleController.setTag("rightScale");
	}

	@NonNull
	@Contract(" -> new")
	@Override
	public Action getController() {
		return new ThreadedAction(leftScaleController, rightScaleController);
	}

	@Override
	public void writeToInstance() {
		instance=this;
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
		if (ScalePositions.probe == recent) {
			back();
		} else {
			probe();
		}
	}

	public void probe() {
		recent = ScalePositions.probe;
		manage(1);
	}

	public void back() {
		recent = ScalePositions.back;
		manage(0.5);
	}

	public void operate(final double position) {
		recent = ScalePositions.probe;
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
