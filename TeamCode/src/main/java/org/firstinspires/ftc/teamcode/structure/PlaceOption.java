package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.HardwareConstants;
import org.firstinspires.ftc.teamcode.actions.Action;
import org.jetbrains.annotations.Contract;

public enum PlaceOption {
	;
	public enum PlacePositionTypes {
		idle,
		decant,
		unknown
	}
	private static PlacePositionTypes recent=PlacePositionTypes.unknown;

	public static PlacePositionTypes recent() {
		return recent;
	}

	private static final class PlaceDecantAction implements Action{
		@Override
		public boolean run() {
			recent=PlacePositionTypes.decant;
			HardwareConstants.place.setPosition(1);
			return false;
		}
	}
	private static final class PlaceIDLEAction implements Action{
		@Override
		public boolean run() {
			recent=PlacePositionTypes.idle;
			HardwareConstants.place.setPosition(0);
			return false;
		}
	}

	@NonNull
	@Contract(" -> new")
	public static Action init(){
		return idle();
	}
	@NonNull
	@Contract(" -> new")
	public static Action decant(){
		return new PlaceDecantAction();
	}
	@NonNull
	@Contract(" -> new")
	public static Action idle(){
		return new PlaceIDLEAction();
	}
}
