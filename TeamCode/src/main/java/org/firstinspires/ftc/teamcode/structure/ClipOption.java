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
	public static ClipPositionTypes recent= ClipPositionTypes.unknown;

	private final static class ClipOpen implements Action{
		@Override
		public boolean run() {
			HardwareConstants.clip.setPosition(0);
			return false;
		}
	}
	private final static class ClipClose implements Action{
		@Override
		public boolean run() {
			HardwareConstants.clip.setPosition(0.5);
			return false;
		}
	}

	@NonNull
	@Contract(" -> new")
	public static Action init(){
		recent=ClipPositionTypes.open;
		return new ClipOpen();
	}
	@NonNull
	@Contract(" -> new")
	public static Action change(){
		switch (recent) {
			case open:case unknown:
				recent= ClipPositionTypes.close;
				return new ClipClose();
			case close:
				recent= ClipPositionTypes.open;
				return new ClipOpen();
		}
		recent= ClipPositionTypes.unknown;
		return new ClipClose();
	}
}
