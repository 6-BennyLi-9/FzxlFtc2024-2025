package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.action.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoCtrl;
import org.jetbrains.annotations.Contract;

public enum ClipOp {
	;

	public enum ClipPositionTypes {
		open, close, unknown
	}

	private static ClipPositionTypes recent = ClipPositionTypes.unknown;
	private static ServoCtrl         clipControl;

	public static void connect() {
		clipControl = new ServoCtrl(HardwareConstants.clip, 0);

		clipControl.setTag("clip");
	}

	public static ClipPositionTypes recent() {
		return recent;
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
		recent = ClipPositionTypes.open;
		clipControl.setTargetPosition(0);
	}

	public static void close() {
		recent = ClipPositionTypes.close;
		clipControl.setTargetPosition(0.5);
	}

	@NonNull
	@Contract(" -> new")
	public static Action getController() {
		return clipControl;
	}

	@NonNull
	public static Action initController() {
		connect();
		Action res = getController();
		init();
		return res;
	}
}
