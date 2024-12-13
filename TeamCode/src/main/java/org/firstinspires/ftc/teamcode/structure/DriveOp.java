package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.pid.PidProcessor;
import org.firstinspires.ftc.teamcode.structure.controllers.ChassisCtrl;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.util.implement.HardwareController;
import org.jetbrains.annotations.Contract;

@Config
public class DriveOp implements HardwareController {
	private static DriveOp instance;
	public static DriveConfig config = DriveConfig.StraightLinear;
	public static ChassisCtrl chassisCtrl;

	public static DriveOp getInstance(){
		return instance;
	}

	public void connect() {
		chassisCtrl = new ChassisCtrl(HardwareConstants.leftFront, HardwareConstants.leftRear, HardwareConstants.rightFront, HardwareConstants.rightRear);

		chassisCtrl.setTag("chassis");
	}

	public static double kP = 0.0001, kI, kD;
	private static double output, targetAngle, currentPowerAngle;
	private static double x, y, turn;

	private static final PidProcessor processor = new PidProcessor(kP, kI, kD, 180);

	private void syncAngle() {
		final double currentAngle = HardwareConstants.imu.getAngularOrientation().firstAngle;
		final double angleErr     = targetAngle - currentAngle;

		switch (config) {
			case PID:
				processor.modify(angleErr);
				output = processor.getCalibrateVal();
				break;
			case SimpleCalibrate:
				output = (targetAngle - currentPowerAngle) * 0.8;
				break;
			case StraightLinear:
			default:
				output = turn;
				break;
		}
	}

	@NonNull
	@Contract(" -> new")
	public Action getController() {
		return chassisCtrl;
	}

	public void sync(final double x, final double y, final double turn, final double bufPower) {
		DriveOp.x = x * bufPower;
		DriveOp.y = y * bufPower;
		DriveOp.turn = turn * bufPower;

		targetAngle += turn * bufPower;
		syncAngle();
		currentPowerAngle += output;
		chassisCtrl.setPowers(x, y, output);
	}

	public void additions(final double x, final double y, final double turn) {
		additions(x, y, turn, 1);
	}

	public void additions(final double x, final double y, final double turn, final double bufPower) {
		sync(DriveOp.x + x, DriveOp.y + y, DriveOp.turn + turn, bufPower);
	}

	public void targetAngleRst() {
		targetAngle = 0;
	}

	@NonNull
	public Action initController() {
		connect();
		return getController();
	}
}
