package org.firstinspires.ftc.teamcode.structure;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.HardwareConstants;
import org.firstinspires.ftc.teamcode.actions.Action;
import org.firstinspires.ftc.teamcode.structure.controllers.ChassisController;
import org.jetbrains.annotations.Contract;

public enum DriveOption {
	;
	private final static ChassisController chassisController;

	static {
		chassisController=new ChassisController(
				HardwareConstants.leftFront,
				HardwareConstants.leftRear,
				HardwareConstants.rightFront,
				HardwareConstants.rightRear
		);

		chassisController.setTag("chassis");
	}

	private final static double kP=-0.12,kI=0.0,kD=0.04;
	private static double lastAngleErr,integralAngle;
	private static double output,targetAngle;
	private static double x,y;

	private static boolean driveUsingPID = true;

	@NonNull
	@Contract(" -> new")
	public static Action cloneAction(){
		return chassisController;
	}

	public static void sync(final double x, final double y, final double turn,final double bufPower){
		chassisController.setPowers(x, y, turn, bufPower);
	}

	public static void setDriveUsingPID(final boolean driveUsingPID) {
		DriveOption.driveUsingPID = driveUsingPID;
	}
}
