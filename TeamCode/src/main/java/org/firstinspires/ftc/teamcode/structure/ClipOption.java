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

	private final static class ClipOpen implements Action{
		@Override
		public boolean run() {
			recent=ClipPositionTypes.open;
			HardwareConstants.clip.setPosition(0);
			return false;
		}
	}
	private final static class ClipClose implements Action{
		@Override
		public boolean run() {
			recent=ClipPositionTypes.close;
			HardwareConstants.clip.setPosition(0.5);
			return false;
		}
	}

	@NonNull
	@Contract(" -> new")
	public static Action init(){
		return new ClipOpen();
	}
	@NonNull
	@Contract(" -> new")
	public static Action change(){
		switch (recent) {
			case open:case unknown:
				return new ClipClose();
			case close:
				return new ClipOpen();
		}
		return new ClipClose();
	}
}
