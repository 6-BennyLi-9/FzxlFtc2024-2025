package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.pid.PidProcessor;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.action.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.ChassisController;
import org.jetbrains.annotations.Contract;

@Config
public enum DriveOption {
	;
	private static ChassisController chassisController;

	public static void connect() {
		chassisController=new ChassisController(
				HardwareConstants.leftFront,
				HardwareConstants.leftRear,
				HardwareConstants.rightFront,
				HardwareConstants.rightRear
		);

		chassisController.setTag("chassis");
	}

	public static double kP=0.12,kI,kD;
	private static double output,targetAngle;
	private static double x,y,turn;

	public  static       boolean      driveUsingPID = true;
	private static final PidProcessor processor     = new PidProcessor(kP,kI,kD,180);

	private static void syncAngle(){
		final double currentAngle =HardwareConstants.imu.getAngularOrientation().firstAngle;
		final double angleErr     = targetAngle - currentAngle;

		if(!driveUsingPID){
			output=turn;
			return;
		}

		processor.modify(angleErr);
		output=processor.getCalibrateVal();
	}

	@NonNull
	@Contract(" -> new")
	public static Action cloneAction(){
		return chassisController;
	}

	public static void sync(final double x, final double y, final double turn){
		sync(x,y,turn,1);
	}
	public static void sync(final double x, final double y, final double turn,final double bufPower){
		DriveOption.x=x * bufPower;
		DriveOption.y=y * bufPower;
		DriveOption.turn=turn *bufPower;

		targetAngle+=turn*bufPower;
		syncAngle();
		chassisController.setPowers(x, y, output, bufPower);
	}
	public static void additions(final double x, final double y, final double turn){
		additions(x,y,turn,1);
	}
	public static void additions(final double x, final double y, final double turn,final double bufPower){
		sync(
				DriveOption.x		+x*bufPower,
				DriveOption.y		+y*bufPower,
				DriveOption.turn	+turn*bufPower
		);
	}

	public static void targetAngleRst(){
		targetAngle=0;
	}

	public static void setDriveUsingPID(final boolean driveUsingPID) {
		DriveOption.driveUsingPID = driveUsingPID;
	}
}
