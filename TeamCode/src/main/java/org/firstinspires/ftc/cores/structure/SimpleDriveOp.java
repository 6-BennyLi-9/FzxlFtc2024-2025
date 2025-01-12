package org.firstinspires.ftc.cores.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.jetbrains.annotations.Contract;

public final class SimpleDriveOp {
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
