package org.firstinspires.ftc.teamcode.ric;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.ric.drives.controls.definition.DriverProgram;
import org.firstinspires.ftc.teamcode.ric.utils.ActionBox;
import org.firstinspires.ftc.teamcode.ric.utils.annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.ric.utils.clients.Client;
import org.firstinspires.ftc.teamcode.ric.utils.enums.RunningMode;

public class Global {
	public static Robot  robot;
	public static Client client;
	public static RunningMode runMode;
	public static ActionBox actionBox;
	public static DriverProgram driverProgram;

	@UserRequirementFunctions
	public static void clear() {
		robot = null;
		client = null;
		runMode = null;
		actionBox = null;
		driverProgram = null;
	}

	public static void setRobot(@NonNull Robot robot) {
		Global.robot = robot;
		Global.driverProgram = robot.drive;
		Global.client = robot.client;
		Global.runMode = robot.runningState;
		Global.actionBox = robot.actionBox;
	}
}
