package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.action.Action;
import org.firstinspires.ftc.teamcode.action.utils.ThreadedAction;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoCtrl;
import org.jetbrains.annotations.Contract;

public enum ArmOp {
	;
	public enum ArmPositionTypes {
		idle,intake,safe,unknown
	}
	private static ArmPositionTypes recent= ArmPositionTypes.unknown;
	private static ServoCtrl leftArmControl, rightArmControl;

	public static void connect() {
		leftArmControl =new ServoCtrl(HardwareConstants.leftArm,0.7);
		rightArmControl =new ServoCtrl(HardwareConstants.rightArm,0.7);

		leftArmControl.setTag("leftArm");
		rightArmControl.setTag("rightArm");
	}

	public static ArmPositionTypes recent() {
		return recent;
	}

	public static void init(){
		safe();
	}

	public static void manage(double position){
		position= Math.min(Math.max(position,0),0.92);
		leftArmControl.setTargetPosition(position+0.08);
		rightArmControl.setTargetPosition(position);
	}

	public static void intake(){
		recent=ArmPositionTypes.intake;
		manage(0.12);
	}
	public static void idle(){
		recent=ArmPositionTypes.idle;
		manage(0.79);
	}
	public static void safe(){
		recent=ArmPositionTypes.safe;
		manage(0.61);
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
	public static Action getController(){
		return new ThreadedAction(leftArmControl, rightArmControl);
	}

	@NonNull
	public static Action initController(){
		connect();
		Action res=getController();
		init();
		return res;
	}
}
