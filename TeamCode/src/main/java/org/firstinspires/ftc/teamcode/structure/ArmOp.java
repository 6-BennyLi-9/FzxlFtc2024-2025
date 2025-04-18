package org.firstinspires.ftc.teamcode.structure;

import static org.firstinspires.ftc.teamcode.HardwareConfigures.ARM_IDLE;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.ARM_INTAKE;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.ARM_LEFT_ADDITION;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.ARM_MAX_POSITION;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.ARM_MIN_POSITION;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.ARM_RISE;
import static org.firstinspires.ftc.teamcode.HardwareConfigures.ARM_SAFE;
import static org.firstinspires.ftc.teamcode.structure.HardwareSituation.*;

import androidx.annotation.NonNull;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.utils.AssembledAction;
import org.betastudio.ftc.util.Labeler;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.controllers.ServoCtrl;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

public class ArmOp implements Interfaces.HardwareController, Interfaces.InitializeRequested, Interfaces.TagOptionsRequired {
	public static ArmPositions recent = ArmPositions.IDLE;
	public static ServoCtrl    leftArmControl, rightArmControl;
	private static ArmOp instance;

	public static ArmOp getInstance() {
		return instance;
	}

	@Override
	public void connect() {
		leftArmControl = new ServoCtrl(HardwareDatabase.leftArm, 0.7);
		rightArmControl = new ServoCtrl(HardwareDatabase.rightArm, 0.7);

		leftArmControl.setTag(Labeler.gen().summon(leftArmControl));
		rightArmControl.setTag(Labeler.gen().summon(rightArmControl));
	}

	@Override
	public void init() {
		safe();
	}

	@NonNull
	@Contract(" -> new")
	@Override
	public Action getController() {
		return new AssembledAction(leftArmControl, rightArmControl);
	}

	@Override
	public void writeToInstance() {
		instance = this;
	}

	public void manage(double position) {
		position = Math.min(Math.max(position, ARM_MIN_POSITION), ARM_MAX_POSITION);
		leftArmControl.setTargetPosition(position + ARM_LEFT_ADDITION);
		rightArmControl.setTargetPosition(position);
	}

	public void intake() {
		recent = ArmPositions.INTAKE;
		manage(ARM_INTAKE);
	}

	public void idle() {
		recent = ArmPositions.IDLE;
		manage(ARM_IDLE);
	}

	public void safe() {
		recent = ArmPositions.SAFE;
		manage(ARM_SAFE);
	}

	public void rise() {
		recent = ArmPositions.RISE;
		manage(ARM_RISE);
	}

	public void flip() {
		switch (recent) {
			case INTAKE:
				idle();
				break;
			case SAFE:
				intake();
				break;
			case IDLE:
			default:
				safe();
				break;
		}
	}

	public void flipIO() {
		if (ArmPositions.INTAKE == Objects.requireNonNull(recent)) {
			rise();
		} else {
			intake();
		}
	}

	public boolean isNotSafe() {
		return ArmPositions.SAFE != recent;
	}

	@NonNull
	public Action initController() {
		connect();
		final Action res = getController();
		init();
		return res;
	}

	@Override
	public String getTag() {
		throw new IllegalStateException("CANNOT GET TAG OF MULTI TAGS");
	}

	@Override
	public void setTag(final String tag) {
		leftArmControl.setTag("left " + tag);
		rightArmControl.setTag("right " + tag);
	}
}
