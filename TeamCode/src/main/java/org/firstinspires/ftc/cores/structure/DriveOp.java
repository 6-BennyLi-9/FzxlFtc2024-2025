package org.firstinspires.ftc.cores.structure;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.specification.HardwareController;
import org.betastudio.ftc.specification.TagOptionsRequired;
import org.firstinspires.ftc.cores.pid.PidProcessor;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.betastudio.ftc.util.Labeler;
import org.firstinspires.ftc.teamcode.controllers.ChassisCtrl;
import org.betastudio.ftc.message.DriveBufMessage;
import org.betastudio.ftc.message.DriveMessage;
import org.jetbrains.annotations.Contract;

@Config
public strictfp class DriveOp implements HardwareController, TagOptionsRequired {
	private static double output, targetAngle, currentPowerAngle, x, y, turn;
	public static final double kP = 0.0001, kI = 0, kD=0;
	private static final PidProcessor processor = new PidProcessor(kP, kI, kD, 180);
	public static DriveMode config = DriveMode.STRAIGHT_LINEAR;
	public static ChassisCtrl chassisCtrl;
	public static DriveBufMessage globalMessage = new DriveBufMessage(0.9, 0.9, 1.3);
	private static DriveOp instance;

	public static DriveOp getInstance() {
		return instance;
	}

	@Override
	public void connect() {
		chassisCtrl = new ChassisCtrl(HardwareDatabase.leftFront, HardwareDatabase.leftRear, HardwareDatabase.rightFront, HardwareDatabase.rightRear);

		chassisCtrl.setTag(Labeler.generate().summonID(chassisCtrl));
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
			case PID:
				processor.modify(angleErr);
				output = processor.getCalibrateVal();
				break;
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
		sync(x, y, turn, new DriveBufMessage(1));
	}

	public void sync(final double x, final double y, final double turn, @NonNull final DriveBufMessage message) {
		DriveOp.x = x * message.valX;
		DriveOp.y = y * message.valY;
		DriveOp.turn = turn * message.valTurn;

		targetAngle += turn * message.valTurn;
		syncAngle();
		currentPowerAngle += output;
		chassisCtrl.send(new DriveMessage(DriveOp.x, DriveOp.y, output));
	}

	public void additions(final double x, final double y, final double turn) {
		additions(x, y, turn, new DriveBufMessage(1));
	}

	public void additions(final double x, final double y, final double turn, @NonNull final DriveBufMessage message) {
		sync(DriveOp.x + x * message.valX, DriveOp.y + y * message.valY, DriveOp.turn + turn * message.valTurn);
	}

	public void turn(final double turn) {
		additions(0, 0, turn);
	}

	public void turn(final double turn, final DriveBufMessage message) {
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
