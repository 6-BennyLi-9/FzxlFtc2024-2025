package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.HardwareConstants;
import org.firstinspires.ftc.teamcode.actions.Action;
import org.jetbrains.annotations.Contract;

public enum ArmOption {
	;
	public enum ArmPositionTypes {
		idle,intake,safe,unknown
	}
	private static ArmPositionTypes recent= ArmPositionTypes.unknown;

	public static ArmPositionTypes recent() {
		return recent;
	}

	private static final class ArmController implements Action {
		@Override
		public boolean run() {
			switch (recent){
				case idle:
					HardwareConstants.leftScale.setPosition(0.86);
					HardwareConstants.rightScale.setPosition(0.86);
					break;
				case intake:
					HardwareConstants.leftScale.setPosition(0.28);
					HardwareConstants.rightScale.setPosition(0.28);
					break;
				case safe:
					HardwareConstants.leftScale.setPosition(0.7);
					HardwareConstants.rightScale.setPosition(0.7);
					break;
				default:
					init();
					return run();
			}
			return true;
		}

		@NonNull
		@Contract(pure = true)
		@Override
		public String paramsString() {
			return "now:idle";
		}
	}

	public static void init(){
		safe();
	}
	public static void intake(){
		recent=ArmPositionTypes.intake;
	}
	public static void idle(){
		recent=ArmPositionTypes.idle;
	}
	public static void safe(){
		recent=ArmPositionTypes.safe;
	}

	public static void flip(){
		switch (recent){
			case intake:
				recent=ArmPositionTypes.idle;
				break;
			case safe:
				recent=ArmPositionTypes.intake;
				break;
			case idle:
			default:
				recent=ArmPositionTypes.safe;
		}
	}

	public static boolean isNotSafe(){
		return ArmPositionTypes.safe != recent;
	}
	@NonNull
	@Contract(" -> new")
	public static Action cloneController(){
		return new ArmController();
	}
}
