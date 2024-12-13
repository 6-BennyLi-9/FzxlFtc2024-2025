package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.utils.ThreadedAction;
import org.firstinspires.ftc.teamcode.structure.controllers.ServoCtrl;
import org.firstinspires.ftc.teamcode.structure.positions.ArmPositionTypes;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.util.implement.HardwareController;
import org.firstinspires.ftc.teamcode.util.implement.InitializeRequested;
import org.firstinspires.ftc.teamcode.util.implement.TagRequested;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

public class ArmOp implements HardwareController, InitializeRequested , TagRequested {
	private static ArmOp instance;
	public static ArmPositionTypes recent = ArmPositionTypes.unknown;
	public static ServoCtrl        leftArmControl, rightArmControl;

	public static ArmOp getInstance(){
		return instance;
	}

	@Override
	public void connect() {
		leftArmControl = new ServoCtrl(HardwareConstants.leftArm, 0.7);
		rightArmControl = new ServoCtrl(HardwareConstants.rightArm, 0.7);

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

	public void manage(double position) {
		position = Math.min(Math.max(position, 0), 0.92);
		leftArmControl.setTargetPosition(position + 0.08);
		rightArmControl.setTargetPosition(position);
	}

	public void intake() {
		recent = ArmPositionTypes.intake;
		manage(0.11);
	}

	public void idle() {
		recent = ArmPositionTypes.idle;
		manage(0.79);
	}

	public void safe() {
		recent = ArmPositionTypes.safe;
		manage(0.61);
	}
	public void flip() {
		switch (recent) {
			case intake:
				idle();
				break;
			case safe:
				intake();
				break;
			case idle:
			default:
				safe();
				break;
		}
	}

	public void flipIO(){
		if (Objects.requireNonNull(recent) == ArmPositionTypes.intake) {
			idle();
		} else {
			init();
		}
	}

	public boolean isNotSafe() {
		return ArmPositionTypes.safe != recent;
	}

	@NonNull
	public Action initController() {
		connect();
		final Action res = getController();
		init();
		return res;
	}

	@Override
	public void setTag(String tag) {
		leftArmControl.setTag("left "+tag);
		rightArmControl.setTag("right "+tag);
	}

	@Override
	public String getTag() {
		throw new IllegalStateException("CANNOT GET TAG OF MULTI TAGS");
	}
}
