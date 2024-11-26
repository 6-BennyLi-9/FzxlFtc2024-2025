package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.action.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoCtrl;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.jetbrains.annotations.Contract;

public enum ClawOp {
	;
	public enum ClawPositionTypes {
		open,
		close,
		unknown
	}
	private static ClawPositionTypes recent = ClawPositionTypes.unknown;
	private static ServoCtrl                clawControl;

	public static void connect() {
		clawControl =new ServoCtrl(HardwareConstants.claw,0);

		clawControl.setTag("claw");
	}

	public static ClawPositionTypes recent() {
		return recent;
	}

	public static void init(){
		close();
	}
	public static void change(){
		switch (recent) {
			case close:
				open();
				break;
			case open:default:
				close();
				break;
		}
	}
	public static void open(){
		recent= ClawPositionTypes.open;
		clawControl.setTargetPosition(0.9);
	}
	public static void close(){
		recent= ClawPositionTypes.close;
		clawControl.setTargetPosition(0.44);
	}

	@NonNull
	@Contract(" -> new")
	public static Action getController(){
		return clawControl;
	}
}
