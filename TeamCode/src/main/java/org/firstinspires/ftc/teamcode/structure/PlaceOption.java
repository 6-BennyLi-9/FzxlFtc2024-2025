package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.HardwareConstants;
import org.firstinspires.ftc.teamcode.actions.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoController;
import org.jetbrains.annotations.Contract;

public enum PlaceOption {
	;
	public enum PlacePositionTypes {
		idle,
		decant,
		unknown
	}
	private static PlacePositionTypes recent=PlacePositionTypes.unknown;
	private static final ServoController placeController;

	static {
		placeController=new ServoController(HardwareConstants.place,0);
	}

	public static PlacePositionTypes recent() {
		return recent;
	}

	public static void init(){
		idle();
	}
	public static void decant(){
		recent=PlacePositionTypes.decant;
		placeController.setTargetPosition(1);
	}
	public static void idle(){
		recent=PlacePositionTypes.idle;
		placeController.setTargetPosition(0);
	}

	@NonNull
	@Contract(" -> new")
	public static Action cloneController(){
		return placeController;
	}
}
