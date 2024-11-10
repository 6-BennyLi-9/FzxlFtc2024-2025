package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.HardwareConstants;
import org.firstinspires.ftc.teamcode.actions.Action;
import org.jetbrains.annotations.Contract;

public enum IOTakesOption {
	;

	public enum IOTakesPositionTypes {
		intake,
		outtake,
		idle
	}

	private static final class IntakeAction implements Action {
		@Override
		public boolean run() {
			HardwareConstants.intake.setPosition(1);
			return false;
		}
	}
	private static final class OuttakeAction implements Action {
		@Override
		public boolean run() {
			HardwareConstants.intake.setPosition(0.3);
			return false;
		}
	}
	private static final class IDLEAction implements Action {
		@Override
		public boolean run() {
			HardwareConstants.intake.setPosition(0.5);
			return false;
		}
	}

	@NonNull
	@Contract(pure = true)
	public static Action IOtakes(@NonNull final IOTakesPositionTypes option){
		switch (option) {
			case intake:
				return new IntakeAction();
			case outtake:
				return new OuttakeAction();
			case idle:
				return new IDLEAction();
		}
		return new IDLEAction();
	}
}
