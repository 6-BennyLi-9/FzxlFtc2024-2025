package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.pid.PidProcessor;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.action.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.ChassisCtrl;
import org.jetbrains.annotations.Contract;

@Config
public enum DriveOp {
	;
	public enum DriveConfig{
		StraightLinear,PID,SimpleCalibrate
	}
	public static  DriveConfig config;
	private static ChassisCtrl chassisCtrl;

	public static void connect() {
		chassisCtrl =new ChassisCtrl(
				HardwareConstants.leftFront,
				HardwareConstants.leftRear,
				HardwareConstants.rightFront,
				HardwareConstants.rightRear
		);

		chassisCtrl.setTag("chassis");
	}

	public static double kP=0.0001,kI,kD;
	private static double output,targetAngle,currentPowerAngle;
	private static double x,y,turn;

	private static final PidProcessor processor     = new PidProcessor(kP,kI,kD,180);

	private static void syncAngle(){
		final double currentAngle =HardwareConstants.imu.getAngularOrientation().firstAngle;
		final double angleErr     = targetAngle - currentAngle;

		switch (config){
			case PID:
				processor.modify(angleErr);
				output=processor.getCalibrateVal();
				break;
			case SimpleCalibrate:
				output=(targetAngle-currentPowerAngle)*0.8;
				break;
			case StraightLinear:
			default:
				output=turn;
				break;
		}
	}

	@NonNull
	@Contract(" -> new")
	public static Action getController(){
		return chassisCtrl;
	}

	public static void sync(final double x, final double y, final double turn,final double bufPower){
		DriveOp.x=x * bufPower;
		DriveOp.y=y * bufPower;
		DriveOp.turn=turn *bufPower;

		targetAngle+=turn*bufPower;
		syncAngle();
		currentPowerAngle+=output;
		chassisCtrl.setPowers(x, y, output);
	}
	public static void additions(final double x, final double y, final double turn){
		additions(x,y,turn,1);
	}
	public static void additions(final double x, final double y, final double turn,final double bufPower){
		sync(
				DriveOp.x		+x,
				DriveOp.y		+y,
				DriveOp.turn	+turn,
				bufPower
		);
	}

	public static void targetAngleRst(){
		targetAngle=0;
	}

	@NonNull
	public static Action initController(){
		connect();
		return getController();
	}
}
