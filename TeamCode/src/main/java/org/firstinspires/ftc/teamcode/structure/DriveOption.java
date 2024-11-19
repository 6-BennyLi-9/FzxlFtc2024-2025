package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.actions.Action;
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

	public static double kP=-0.12,kI,kD=0.04;
	private static double lastAngleErr,integralAngle;
	private static double output,targetAngle;
	private static double x,y,turn;

	private static boolean driveUsingPID = true;

	private static void syncAngle(){
		final double currentAngle=HardwareConstants.imu.getAngularOrientation().firstAngle;
		double angleErr= targetAngle-currentAngle;

		if(!driveUsingPID){
			output=turn;
			return;
		}

		while (-180 > angleErr) angleErr+=360;
		while (180 < angleErr)  angleErr-=360;
		final double differentiation=angleErr-lastAngleErr;
		integralAngle+=angleErr;
		lastAngleErr=angleErr;

		output=(kP*angleErr+kI*integralAngle+kD*differentiation)/15;
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
		DriveOption.x=x;
		DriveOption.y=y;
		DriveOption.turn=turn;

		targetAngle+=turn;
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

	public static void setDriveUsingPID(final boolean driveUsingPID) {
		DriveOption.driveUsingPID = driveUsingPID;
	}
}
