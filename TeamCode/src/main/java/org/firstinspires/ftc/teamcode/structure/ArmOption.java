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

	private static final class ArmIDLEAction implements Action {
		@Override
		public boolean run() {
			HardwareConstants.leftScale.setPosition(0.86);
			HardwareConstants.rightScale.setPosition(0.86);
			return false;
		}
	}
	private static final class ArmIntakeAction implements Action {
		@Override
		public boolean run() {
			HardwareConstants.leftScale.setPosition(0.28);
			HardwareConstants.rightScale.setPosition(0.28);
			return false;
		}
	}
	private static final class ArmSafeAction implements Action {
		@Override
		public boolean run() {
			HardwareConstants.leftScale.setPosition(0.7);
			HardwareConstants.rightScale.setPosition(0.7);
			return false;
		}
	}

	@NonNull
	@Contract(" -> new")
	public static Action init(){
		recent= ArmPositionTypes.idle;
		return new ArmIDLEAction();
	}

	@NonNull
	@Contract(" -> new")
	public static Action intake(){
		recent= ArmPositionTypes.intake;
		return new ArmIntakeAction();
	}
	@NonNull
	@Contract(" -> new")
	public static Action idle(){
		recent= ArmPositionTypes.idle;
		return new ArmIDLEAction();
	}
	@NonNull
	@Contract(" -> new")
	public static Action safe(){
		recent=ArmPositionTypes.safe;
		return new ArmSafeAction();
	}

	@NonNull
	@Contract(" -> new")
	public static Action flip(){
		if(ArmPositionTypes.intake == recent){
			return idle();
		}else{
			return intake();
		}
	}

	public static boolean isNotSafe(){
		return ArmPositionTypes.safe != recent;
	}
}
