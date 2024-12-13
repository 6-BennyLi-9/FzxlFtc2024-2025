package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoCtrl;
import org.firstinspires.ftc.teamcode.structure.positions.ClawPositionTypes;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.util.implement.HardwareController;
import org.firstinspires.ftc.teamcode.util.implement.InitializeRequested;
import org.jetbrains.annotations.Contract;

public class ClawOp implements HardwareController , InitializeRequested {
	private static ClawOp instance;
	public static ClawPositionTypes recent = ClawPositionTypes.unknown;
	public static ServoCtrl         clawControl;

	public static ClawOp getInstance(){
		return instance;
	}

	@Override
	public void connect() {
		clawControl = new ServoCtrl(HardwareConstants.claw, 0);

		clawControl.setTag("claw");
	}

	@Override
	public void init() {
		open();
	}

	public void change() {
		switch (recent) {
			case close:
				open();
				break;
			case open:
			default:
				close();
				break;
		}
	}

	public void open() {
		recent = ClawPositionTypes.open;
		clawControl.setTargetPosition(0.65);
	}

	public void close() {
		recent = ClawPositionTypes.close;
		clawControl.setTargetPosition(0.45);
	}

	@NonNull
	@Contract(" -> new")
	public Action getController() {
		return clawControl;
	}

	@NonNull
	public Action initController() {
		connect();
		final Action res = getController();
		init();
		return res;
	}
}
