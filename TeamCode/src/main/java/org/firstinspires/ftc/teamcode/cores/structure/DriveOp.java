package org.firstinspires.ftc.teamcode.cores.structure;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.util.Labeler;
import org.betastudio.ftc.util.message.DriveBufMsg;
import org.betastudio.ftc.util.message.DriveMsg;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.controllers.ChassisCtrl;
import org.jetbrains.annotations.Contract;

@Config
public strictfp class DriveOp implements Interfaces.HardwareController, Interfaces.TagOptionsRequired {
	public static       ChassisCtrl chassisCtrl;
	private static      double      x;
	private static      double      y;
	private static      double      turn;
	private static      DriveOp     instance;

	public static DriveOp getInstance() {
		return instance;
	}

	@Override
	public void connect() {
		chassisCtrl = new ChassisCtrl(HardwareDatabase.leftFront, HardwareDatabase.leftRear, HardwareDatabase.rightFront, HardwareDatabase.rightRear);

		chassisCtrl.setTag(Labeler.gen().summon(chassisCtrl));
	}

	@NonNull
	@Contract(" -> new")
	@Override
	public Action getController() {
		return chassisCtrl;
	}

	@Override
	public void writeToInstance() {
		instance = this;
	}

	public void sync(final double x, final double y, final double turn) {
		sync(x, y, turn, new DriveBufMsg(1));
	}

	public void sync(final double x, final double y, final double turn, @NonNull final DriveBufMsg message) {
		chassisCtrl.sendMsg(new DriveMsg(x * message.valX, y * message.valY, turn * message.valTurn));
	}

	public void additions(final double x, final double y, final double turn) {
		additions(x, y, turn, new DriveBufMsg(1));
	}

	public void additions(final double x, final double y, final double turn, @NonNull final DriveBufMsg message) {
		sync(DriveOp.x + x * message.valX, DriveOp.y + y * message.valY, DriveOp.turn + turn * message.valTurn);
	}

	public void turn(final double turn) {
		additions(0, 0, turn);
	}

	public void turn(final double turn, final DriveBufMsg message) {
		additions(0, 0, turn, message);
	}

	@NonNull
	public Action initController() {
		connect();
		return getController();
	}

	@Override
	public String getTag() {
		return chassisCtrl.getTag();
	}

	@Override
	public void setTag(final String tag) {
		chassisCtrl.setTag(tag);
	}
}
