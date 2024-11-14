package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.HardwareConstants;
import org.firstinspires.ftc.teamcode.actions.Action;
import org.jetbrains.annotations.Contract;

public enum DriveOption {
	;
	private final static double kP=-0.12,kI=0.0,kD=0.04;
	private static double lastAngleErr,integralAngle;
	private static double output,targetAngle;
	private static double x,y;

	private static boolean driveUsingPID = true;

	private static void syncAngle(){
		if(!driveUsingPID){
			output=targetAngle;
			return;
		}

		double angleErr= targetAngle-HardwareConstants.imu.getAngularOrientation().firstAngle;

		while (-180 > angleErr) angleErr+=360;
		while (180 < angleErr)  angleErr-=360;
		final double differentiation=angleErr-lastAngleErr;
		integralAngle+=angleErr;
		lastAngleErr=angleErr;

		output=(kP*angleErr+kI*integralAngle+kD*differentiation)/15;
	}

	private static final class DriveAction implements Action{
		@Override
		public boolean run() {
			syncAngle();
			HardwareConstants.leftFront.setPower	(y+x-output);
			HardwareConstants.leftRear.setPower		(y-x-output);
			HardwareConstants.rightFront.setPower	(y-x+output);
			HardwareConstants.rightRear.setPower	(y+x+output);
			return true;
		}

		@NonNull
		@Contract(pure = true)
		@Override
		public String paramsString() {
			return "x:"+x+",y"+y+",heading:"+output;
		}
	}

	@NonNull
	@Contract(" -> new")
	public static Action cloneAction(){
		return new DriveAction();
	}

	public static void sync(final double x, final double y, final double turn,final double bufPower){
		DriveOption.x=x*bufPower;
		DriveOption.y=y*bufPower;
		targetAngle=turn*bufPower;
	}

	public static void setDriveUsingPID(boolean driveUsingPID) {
		DriveOption.driveUsingPID = driveUsingPID;
	}
}
