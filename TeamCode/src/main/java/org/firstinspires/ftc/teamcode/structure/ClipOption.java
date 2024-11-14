package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.HardwareConstants;
import org.firstinspires.ftc.teamcode.actions.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoController;
import org.jetbrains.annotations.Contract;

public enum ClipOption {
	;

	public enum ClipPositionTypes {
		open,
		close,
		unknown
	}
	private static ClipPositionTypes recent= ClipPositionTypes.unknown;
	private static final ServoController clipControl;
	static {
		clipControl=new ServoController(HardwareConstants.clip,0);
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
			case open:default:
				close();
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
