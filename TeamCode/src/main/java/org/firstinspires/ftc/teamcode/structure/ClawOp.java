package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.action.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoCtrl;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.jetbrains.annotations.Contract;

public enum ClawOp {
	;

	public enum ClawPositionTypes {
		open, close, unknown
	}

	public static ClawPositionTypes recent = ClawPositionTypes.unknown;
	public static ServoCtrl         clawControl;

	public static void connect() {
		clawControl = new ServoCtrl(HardwareConstants.claw, 0);

		clawControl.setTag("claw");
	}

	public static void init() {
		open();
	}

	public static void change() {
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

	public static void open() {
		recent = ClawPositionTypes.open;
		clawControl.setTargetPosition(0.65);
	}

	public static void close() {
		recent = ClawPositionTypes.close;
		clawControl.setTargetPosition(0.45);
	}

	@NonNull
	@Contract(" -> new")
	public static Action getController() {
		return clawControl;
	}

	@NonNull
	public static Action initController() {
		connect();
		final Action res = getController();
		init();
		return res;
	}
}
