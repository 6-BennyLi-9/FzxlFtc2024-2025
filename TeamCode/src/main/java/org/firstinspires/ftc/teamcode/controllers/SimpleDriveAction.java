package org.firstinspires.ftc.teamcode.controllers;

import static org.firstinspires.ftc.teamcode.Hardwares.leftFront;
import static org.firstinspires.ftc.teamcode.Hardwares.leftRear;
import static org.firstinspires.ftc.teamcode.Hardwares.rightFront;
import static org.firstinspires.ftc.teamcode.Hardwares.rightRear;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.jetbrains.annotations.Contract;

public final class SimpleDriveAction implements Action {
	private final double x;
	private final double y;
	private final double turn;

	public SimpleDriveAction(final double x, final double y, final double turn) {
		this.x = x;
		this.y = y;
		this.turn = turn;
	}

	@Override
	public boolean activate() {
		leftFront.setPower(y + x - turn);
		leftRear.setPower(y - x - turn);
		rightFront.setPower(y - x + turn);
		rightRear.setPower(y + x + turn);
		return false;
	}

	@NonNull
	@Contract(pure = true)
	@Override
	public String paramsString() {
		return "x:" + x + ",y:" + y + ",turn:" + turn;
	}
}
