package org.firstinspires.ftc.cores.structure;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.interfaces.HardwareController;
import org.betastudio.ftc.interfaces.Taggable;
import org.firstinspires.ftc.cores.pid.PidProcessor;
import org.firstinspires.ftc.teamcode.HardwareDatabase;
import org.firstinspires.ftc.teamcode.controllers.ChassisCtrl;
import org.firstinspires.ftc.teamcode.message.DriveBufMessage;
import org.jetbrains.annotations.Contract;

@Config
public class DriveOp implements HardwareController, Taggable {
	public static  DriveMode   config = DriveMode.STRAIGHT_LINEAR;
	public static  ChassisCtrl chassisCtrl;
	public static double kP = 0.0001, kI, kD;
	private static final PidProcessor processor = new PidProcessor(kP, kI, kD, 180);
	private static DriveOp     instance;
	private static double output, targetAngle, currentPowerAngle;
	private static double x, y, turn;
	public static DriveBufMessage globalMessage=new DriveBufMessage(1,1,1.3);

	public static DriveOp getInstance() {
		return instance;
	}

	@Override
	public void connect() {
		chassisCtrl = new ChassisCtrl(HardwareDatabase.leftFront, HardwareDatabase.leftRear, HardwareDatabase.rightFront, HardwareDatabase.rightRear);

		chassisCtrl.setTag("chassis");
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

	public void sync(final double x, final double y, final double turn, @NonNull final DriveBufMessage message) {
		DriveOp.x = x * message.bufX;
		DriveOp.y = y * message.bufY;
		DriveOp.turn = turn * message.bufTurn;

		targetAngle += turn * message.bufTurn;
		syncAngle();
		currentPowerAngle += output;
		chassisCtrl.setPowers(x, y, output);
	}

	public void additions(final double x, final double y, final double turn) {
		additions(x, y, turn, new DriveBufMessage(1));
	}

	public void additions(final double x, final double y, final double turn, final DriveBufMessage message) {
		sync(DriveOp.x + x, DriveOp.y + y, DriveOp.turn + turn, message);
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
	public void setTag(String tag) {
		chassisCtrl.setTag(tag);
	}
}
