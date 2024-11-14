package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.HardwareConstants;
import org.firstinspires.ftc.teamcode.actions.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoController;
import org.jetbrains.annotations.Contract;

public enum IOTakesOption {
	;

	public enum IOTakesPositionTypes {
		intake,
		outtake,
		idle
	}

	private static IOTakesPositionTypes recent;
	private final static ServoController intakeController;

	static {
		intakeController=new ServoController(HardwareConstants.intake,0.5);
	}


	public static void idle(){
		recent=IOTakesPositionTypes.idle;
		intakeController.setTargetPosition(0.5);
	}
	public static void intake(){
		recent=IOTakesPositionTypes.intake;
		intakeController.setTargetPosition(1);
	}
	public static void outtake(){
		recent=IOTakesPositionTypes.outtake;
		intakeController.setTargetPosition(0);
	}

	@NonNull
	@Contract(" -> new")
	public static Action cloneController(){
		return intakeController;
	}
}
