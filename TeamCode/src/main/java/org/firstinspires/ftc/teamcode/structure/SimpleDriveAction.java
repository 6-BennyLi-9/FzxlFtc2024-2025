package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.jetbrains.annotations.Contract;

final class SimpleDriveAction implements Action {
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
