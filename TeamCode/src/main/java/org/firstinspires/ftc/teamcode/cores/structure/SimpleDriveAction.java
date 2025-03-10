package org.firstinspires.ftc.teamcode.cores.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.jetbrains.annotations.Contract;

public final class SimpleDriveAction implements Action {
	private final double x, y, turn;

	SimpleDriveAction(final double x, final double y, final double turn) {
		this.x = x;
		this.y = y;
		this.turn = turn;
	}

	@Override
	public boolean activate() {
		HardwareDatabase.leftFront.setPower(y + x - turn);
		HardwareDatabase.leftRear.setPower(y - x - turn);
		HardwareDatabase.rightFront.setPower(y - x + turn);
		HardwareDatabase.rightRear.setPower(y + x + turn);
		return false;
	}

	@NonNull
	@Contract(pure = true)
	@Override
	public String paramsString() {
		return "x:" + x + ",y:" + y + ",turn:" + turn;
	}
}
