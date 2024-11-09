package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.HardwareConstants;
import org.firstinspires.ftc.teamcode.actions.Action;
import org.jetbrains.annotations.Contract;

public enum ScaleOption {
	;
	public enum ScalePositionTypes{
		idle,intake,safe,unknown
	}
	public static ScalePositionTypes recent=ScalePositionTypes.unknown;

	private static final class ScaleIDLEAction implements Action {
		@Override
		public boolean run() {
			HardwareConstants.leftScale.setPosition(1);
			HardwareConstants.rightScale.setPosition(0.5);
			return false;
		}
	}
	private static final class ScaleIntakeAction implements Action {
		@Override
		public boolean run() {
			HardwareConstants.leftScale.setPosition(0.5);
			HardwareConstants.rightScale.setPosition(1);
			return false;
		}
	}
	private static final class ScaleSafeAction implements Action {
		@Override
		public boolean run() {
			HardwareConstants.leftScale.setPosition(0.8);
			HardwareConstants.rightScale.setPosition(0.7);
			return false;
		}
	}

	@NonNull
	@Contract(" -> new")
	public static Action scaleInitAction(){
		recent=ScalePositionTypes.idle;
		return new ScaleIDLEAction();
	}
	@NonNull
	@Contract(" -> new")
	public static Action scaleSafeOption(){
		recent=ScalePositionTypes.safe;
		return new ScaleSafeAction();
	}
	@NonNull
	@Contract(" -> new")
	public static Action scaleIntakeOption(){
		recent=ScalePositionTypes.intake;
		return new ScaleIntakeAction();
	}
	@NonNull
	@Contract(" -> new")
	public static Action scaleIDLEOption(){
		recent=ScalePositionTypes.idle;
		return new ScaleIDLEAction();
	}
}
