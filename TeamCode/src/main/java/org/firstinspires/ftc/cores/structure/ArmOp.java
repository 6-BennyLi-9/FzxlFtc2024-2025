package org.firstinspires.ftc.cores.structure;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.utils.ThreadedAction;
import org.firstinspires.ftc.teamcode.controllers.ServoCtrl;
import org.firstinspires.ftc.cores.structure.positions.ArmPositions;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.interfaces.HardwareController;
import org.firstinspires.ftc.teamcode.interfaces.InitializeRequested;
import org.firstinspires.ftc.teamcode.interfaces.TagRequested;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

public class ArmOp implements HardwareController, InitializeRequested , TagRequested {
	public static  ArmPositions recent = ArmPositions.idle;
	public static  ServoCtrl        leftArmControl, rightArmControl;
	private static ArmOp            instance;

	public static ArmOp getInstance(){
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
		instance=this;
	}

	public void manage(double position) {
		position = Math.min(Math.max(position, 0), 0.92);
		leftArmControl.setTargetPosition(position + 0.08);
		rightArmControl.setTargetPosition(position);
	}

	public void intake() {
		recent = ArmPositions.intake;
		manage(0.11);
	}

	public void idle() {
		recent = ArmPositions.idle;
		manage(0.79);
	}

	public void safe() {
		recent = ArmPositions.safe;
		manage(0.61);
	}

	public void rise(){
		recent = ArmPositions.rise;
		manage(0.37);
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
		if (Objects.requireNonNull(recent) == ArmPositions.intake) {
			rise();
		} else {
			intake();
		}
	}

	public boolean isNotSafe() {
		return ArmPositions.safe != recent;
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
