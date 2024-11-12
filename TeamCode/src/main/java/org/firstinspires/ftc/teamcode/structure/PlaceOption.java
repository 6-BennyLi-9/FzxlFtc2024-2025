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

	private static final class PlaceController implements Action{
		@Override
		public boolean run() {
			switch (recent){
				case idle:
					HardwareConstants.place.setPosition(0);
					break;
				case decant:
					HardwareConstants.place.setPosition(1);
					break;
				case unknown: default:
					init();
					return run();
			}
			return false;
		}

		@NonNull
		@Contract(pure = true)
		@Override
		public String paramsString() {
			return "now:decant";
		}
	}

	public static void init(){
		idle();
	}
	public static void decant(){
		recent=PlacePositionTypes.decant;
	}
	public static void idle(){
		recent=PlacePositionTypes.idle;
	}

	@NonNull
	@Contract(" -> new")
	public static Action cloneController(){
		return new PlaceController();
	}
}
