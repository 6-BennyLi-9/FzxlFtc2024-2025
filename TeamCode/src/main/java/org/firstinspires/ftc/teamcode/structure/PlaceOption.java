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
	public static PlacePositionTypes recent=PlacePositionTypes.unknown;

	private static final class PlaceDecantAction implements Action{
		@Override
		public boolean run() {
			HardwareConstants.place.setPosition(1);
			return false;
		}
	}
	private static final class PlaceIDLEAction implements Action{
		@Override
		public boolean run() {
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
		recent=PlacePositionTypes.decant;
		return new PlaceDecantAction();
	}
	@NonNull
	@Contract(" -> new")
	public static Action idle(){
		recent=PlacePositionTypes.idle;
		return new PlaceIDLEAction();
	}
}
