package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.action.Action;
import org.jetbrains.annotations.Contract;

public enum SimpleDriveOp {
	;

	private static final class SimpleDriveAction implements Action {
		private final double x, y, turn;

		SimpleDriveAction(final double x, final double y, final double turn) {
			this.x = x;
			this.y = y;
			this.turn = turn;
		}

		@Override
		public boolean run() {
			HardwareConstants.leftFront.setPower(y + x - turn);
			HardwareConstants.leftRear.setPower(y - x - turn);
			HardwareConstants.rightFront.setPower(y - x + turn);
			HardwareConstants.rightRear.setPower(y + x + turn);
			return false;
		}

		@NonNull
		@Contract(pure = true)
		@Override
		public String paramsString() {
			return "x:" + x + ",y:" + y + ",turn:" + turn;
		}
	}

	@NonNull
	@Contract("_, _, _ -> new")
	public static Action build(final double x, final double y, final double turn) {
		return new SimpleDriveAction(x, y, turn);
	}

	@NonNull
	@Contract("_, _, _, _ -> new")
	public static Action build(final double x, final double y, final double turn, final double bufPower) {
		return new SimpleDriveAction(x * bufPower, y * bufPower, turn * bufPower);
	}
}
