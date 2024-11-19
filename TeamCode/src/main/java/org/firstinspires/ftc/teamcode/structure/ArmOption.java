package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.HardwareConstants;
import org.firstinspires.ftc.teamcode.actions.Action;
import org.firstinspires.ftc.teamcode.actions.utils.ThreadedAction;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoController;
import org.jetbrains.annotations.Contract;

public enum ArmOption {
	;
	public enum ArmPositionTypes {
		idle,intake,safe,unknown
	}
	private static ArmPositionTypes recent= ArmPositionTypes.unknown;
	private static ServoController leftArmControl, rightArmControl;

	public static void connect() {
		leftArmControl =new ServoController(HardwareConstants.leftArm,0.7);
		rightArmControl =new ServoController(HardwareConstants.rightArm,0.7);

		leftArmControl.setTag("leftArm");
		rightArmControl.setTag("rightArm");
	}

	public static ArmPositionTypes recent() {
		return recent;
	}

	public static void init(){
		safe();
	}
	public static void intake(){
		recent=ArmPositionTypes.intake;
		leftArmControl.setTargetPosition(0.3);
		rightArmControl.setTargetPosition(0.3);
	}
	public static void idle(){
		recent=ArmPositionTypes.idle;
		leftArmControl.setTargetPosition(0.86);
		rightArmControl.setTargetPosition(0.86);
	}
	public static void safe(){
		recent=ArmPositionTypes.safe;
		leftArmControl.setTargetPosition(0.75);
		rightArmControl.setTargetPosition(0.75);
	}

	public static void flip(){
		switch (recent){
			case intake:
				idle();
				break;
			case safe:
				intake();
				break;
			case idle:
			default:
				safe();
				break;
		}
	}

	public static boolean isNotSafe(){
		return ArmPositionTypes.safe != recent;
	}
	@NonNull
	@Contract(" -> new")
	public static Action cloneController(){
		return new ThreadedAction(leftArmControl, rightArmControl);
	}
}
