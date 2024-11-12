package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.HardwareConstants;
import org.firstinspires.ftc.teamcode.actions.Action;
import org.jetbrains.annotations.Contract;

public enum ClipOption {
	;

	public enum ClipPositionTypes {
		open,
		close,
		unknown
	}
	private static ClipPositionTypes recent= ClipPositionTypes.unknown;

	public static ClipPositionTypes recent() {
		return recent;
	}

	private final static class ClipController implements Action{
		@Override
		public boolean run() {
			switch (recent) {
				case open:
					HardwareConstants.clip.setPosition(0);
					break;
				case close:
					HardwareConstants.clip.setPosition(0.5);
					break;
				case unknown: default:
					init();
					return run();
			}
			return true;
		}

		@NonNull
		@Contract(pure = true)
		@Override
		public String paramsString() {
			return "now:"+recent.name();
		}
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
	}
	private static void close(){
		recent=ClipPositionTypes.close;
	}

	@NonNull
	@Contract(" -> new")
	public static Action cloneController(){
		return new ClipController();
	}
}
