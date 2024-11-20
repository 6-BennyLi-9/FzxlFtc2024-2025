package org.firstinspires.ftc.teamcode.util;

import static org.firstinspires.ftc.teamcode.util.Robot.*;

import org.firstinspires.ftc.teamcode.client.TelemetryClient;

public enum GamepadRequestMemories {
	;
	/**输入样本*/
	public static boolean intakeSamples;
	/**吐出样本（小功率）*/
	public static boolean outtakeSamples;
	/**翻转收取杆*/
	public static boolean flipArms;
	/**电梯低筐预备*/
	public static boolean liftDecantLow;
	/**电梯高筐预备*/
	public static boolean liftDecantHigh;
	/**电梯闲置（下）*/
	public static boolean liftIDLE;
	/**电梯挂样本准备*/
	public static boolean liftHighSuspendPrepare;
	/**倒筐*/
	public static boolean decant;
	/**挂样本*/
	public static boolean suspend;
	/**打开/关闭样本夹*/
	public static boolean clipOption;
	/**伸缩吸取滑轨*/
	public static boolean probe;

	public static boolean flipArmsRan,clipOptionRan,probeRan;

	public static void syncRequests(){
		intakeSamples 	=  0.3 < gamepad2.left_stick_x;
		outtakeSamples 	= -0.3 > gamepad2.left_stick_x;
		flipArms 		= gamepad2.a;
		liftDecantLow   = gamepad2.dpad_up;
		liftDecantHigh  = gamepad2.left_bumper;
		liftHighSuspendPrepare = gamepad2.dpad_right;
		liftIDLE      	= gamepad2.dpad_down;
		decant			= gamepad2.x;
		suspend			= gamepad2.y;
		clipOption		= gamepad2.b;
		probe			= gamepad2.right_bumper;

		if(!flipArms){
			flipArmsRan=false;
		}
		if(!clipOption){
			clipOptionRan=false;
		}
		if(!probe){
			probeRan=false;
		}
	}

	public static void printValues(){
		final TelemetryClient instance=TelemetryClient.getInstance();
		instance.changeData("intakeSamples",intakeSamples);
		instance.changeData("outtakeSamples",outtakeSamples);
		instance.changeData("flipArms",flipArms);
		instance.changeData("liftDecantLow",liftDecantLow);
		instance.changeData("liftDecantHigh",liftDecantHigh);
		instance.changeData("liftHighSuspendPrepare", liftHighSuspendPrepare);
		instance.changeData("liftIDLE",liftIDLE);
		instance.changeData("decant",decant);
		instance.changeData("suspend",suspend);
		instance.changeData("clipOption",clipOption);
		instance.changeData("probe",probe);

		instance.changeData("--------ran---------","");
		instance.changeData("flipArmsRan",flipArmsRan);
		instance.changeData("clipOptionRan",clipOptionRan);
		instance.changeData("probeRan",probeRan);
	}
}
