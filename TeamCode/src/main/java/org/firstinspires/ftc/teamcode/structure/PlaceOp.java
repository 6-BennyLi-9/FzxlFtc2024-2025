package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.action.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoCtrl;
import org.jetbrains.annotations.Contract;

public enum PlaceOp {
	;
	public enum PlacePositionTypes {
		idle,
		decant,
		unknown
	}
	private static PlacePositionTypes recent=PlacePositionTypes.unknown;
	private static ServoCtrl placeController;

	public static void connect() {
		placeController=new ServoCtrl(HardwareConstants.place,0);

		placeController.setTag("place");
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
	public static void safe(){
		placeController.setTargetPosition(0.28);
	}

	@NonNull
	@Contract(" -> new")
	public static Action getController(){
		return placeController;
	}

	@NonNull
	public static Action initController(){
		connect();
		Action res=getController();
		init();
		return res;
	}
}
