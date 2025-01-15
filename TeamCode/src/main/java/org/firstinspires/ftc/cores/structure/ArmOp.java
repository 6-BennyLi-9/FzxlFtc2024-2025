package org.firstinspires.ftc.cores.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.utils.ThreadedAction;
import org.betastudio.ftc.interfaces.HardwareController;
import org.betastudio.ftc.interfaces.InitializeRequested;
import org.betastudio.ftc.interfaces.InstanceRequired;
import org.betastudio.ftc.interfaces.TagOptionsRequired;
import org.firstinspires.ftc.cores.structure.positions.ArmPositions;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.controllers.ServoCtrl;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

public class ArmOp implements HardwareController, InitializeRequested, TagOptionsRequired, InstanceRequired<ArmOp> {
	public static ArmPositions recent = ArmPositions.IDLE;
	public static ServoCtrl    leftArmControl, rightArmControl;
	private static ArmOp instance;

	@Override
	public ArmOp getInstance() {
		return instance;
	}

	@Override
	public void setInstance(ArmOp instance) {
		ArmOp.instance = instance;
	}

	public static ArmOp getOp() {
		return instance;
	}

	@Override
	public void connect() {
		leftArmControl = new ServoCtrl(HardwareDatabase.leftArm, 0.7);
		rightArmControl = new ServoCtrl(HardwareDatabase.rightArm, 0.7);

		leftArmControl.setTag("leftArm");
		rightArmControl.setTag("rightArm");
	}

	@Override
	public void init() {
		safe();
	}

	@NonNull
	@Contract(" -> new")
	@Override
	public Action getController() {
		return new ThreadedAction(leftArmControl, rightArmControl);
	}

	@Override
	public void writeToInstance() {
		instance = this;
	}

	public void manage(double position) {
		position = Math.min(Math.max(position, 0), 0.92);
		leftArmControl.setTargetPosition(position + 0.08);
		rightArmControl.setTargetPosition(position);
	}

	public void intake() {
		recent = ArmPositions.INTAKE;
		manage(0.11);
	}

	public void idle() {
		recent = ArmPositions.IDLE;
		manage(0.79);
	}

	public void safe() {
		recent = ArmPositions.SAFE;
		manage(0.61);
	}

	public void rise() {
		recent = ArmPositions.RISE;
		manage(0.37);
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
		if (Objects.requireNonNull(recent) == ArmPositions.INTAKE) {
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
	public void setTag(String tag) {
		leftArmControl.setTag("left " + tag);
		rightArmControl.setTag("right " + tag);
	}
}
