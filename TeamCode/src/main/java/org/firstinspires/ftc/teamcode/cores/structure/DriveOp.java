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
	public static final double kP = 0.0001, kI = 0, kD = 0;
	public static        DriveMode    config        = DriveMode.STRAIGHT_LINEAR;
	public static        ChassisCtrl  chassisCtrl;
	public static        DriveBufMsg  globalMessage = new DriveBufMsg(0.9, 0.9, 1.3);
	private static       double       output, targetAngle, currentPowerAngle, x, y, turn;
	private static DriveOp instance;

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

	private void syncAngle() {
		final double currentAngle = HardwareDatabase.imu.getAngularOrientation().firstAngle;
		final double angleErr     = targetAngle - currentAngle;

		switch (config) {
			case SIMPLE_CALIBRATE:
				output = (targetAngle - currentPowerAngle) * 0.8;
				break;
			case STRAIGHT_LINEAR:
			default:
				output = turn;
				break;
		}
	}

	public void sync(final double x, final double y, final double turn) {
		sync(x, y, turn, new DriveBufMsg(1));
	}

	public void sync(final double x, final double y, final double turn, @NonNull final DriveBufMsg message) {
		DriveOp.x = x * message.valX;
		DriveOp.y = y * message.valY;
		DriveOp.turn = turn * message.valTurn;

		targetAngle += turn * message.valTurn;
		syncAngle();
		currentPowerAngle += output;
		chassisCtrl.sendMsg(new DriveMsg(DriveOp.x, DriveOp.y, output));
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

	public void targetAngleRst() {
		targetAngle = 0;
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
