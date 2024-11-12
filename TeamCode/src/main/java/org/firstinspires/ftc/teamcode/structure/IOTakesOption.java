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

	private static IOTakesPositionTypes recent;

	private static final class IOTakesController implements Action {
		@Override
		public boolean run() {
			switch (recent) {
				case intake:
					HardwareConstants.intake.setPosition(1);
					break;
				case outtake:
					HardwareConstants.intake.setPosition(0);
					break;
				case idle:
					HardwareConstants.intake.setPosition(0.5);
					break;
				default:

			}
			return false;
		}

		@NonNull
		@Contract(pure = true)
		@Override
		public String paramsString() {
			return "now:intake";
		}
	}

	public static void sync(@NonNull final IOTakesPositionTypes option){
		recent=option;
	}
	@NonNull
	@Contract(" -> new")
	public static Action cloneController(){
		return new IOTakesController();
	}
}
