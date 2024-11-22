package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.action.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoCtrl;
import org.jetbrains.annotations.Contract;

public enum ClipOp {
	;

	public enum ClipPositionTypes {
		open,
		close,
		unknown
	}
	private static ClipPositionTypes recent= ClipPositionTypes.unknown;
	private static ServoCtrl clipControl;

	public static void connect() {
		clipControl=new ServoCtrl(HardwareConstants.clip,0);

		clipControl.setTag("clip");
	}

	public static ClipPositionTypes recent() {
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
		recent=ClipPositionTypes.open;
		clipControl.setTargetPosition(0);
	}
	private static void close(){
		recent=ClipPositionTypes.close;
		clipControl.setTargetPosition(0.5);
	}

	@NonNull
	@Contract(" -> new")
	public static Action cloneController(){
		return clipControl;
	}
}
