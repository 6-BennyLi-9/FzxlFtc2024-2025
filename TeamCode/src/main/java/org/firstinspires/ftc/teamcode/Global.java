package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ThreadPool;

public final class Global {
	public static ThreadPool coreThreadPool;
	public static Gamepad gamepad1,gamepad2;
	public static RunMode currentMode;

	public static void registerGamepad(Gamepad gamepad1,Gamepad gamepad2){
		Global.gamepad1=gamepad1;
		Global.gamepad2=gamepad2;
	}
}
