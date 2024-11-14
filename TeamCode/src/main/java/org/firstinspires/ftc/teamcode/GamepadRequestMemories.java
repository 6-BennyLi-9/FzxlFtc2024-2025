package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Robot.*;

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
	public static boolean liftHighSuspend;
	/**倒筐*/
	public static boolean decant;
	/**挂样本*/
	public static boolean suspend;
	/**打开/关闭样本夹*/
	public static boolean clipOption;
	/**伸缩吸取滑轨*/
	public static boolean probe;

	private static boolean lstGamepad2a,lstGamepad2b,lstGamepad2Right_bumper;

	public static void syncRequests(){
		GamepadLatest.sync(gamepad2);

		intakeSamples 	=  0.3 < gamepad2.left_stick_x;
		outtakeSamples 	= -0.3 > gamepad2.left_stick_x;
		flipArms 		= GamepadLatest.a.value && !lstGamepad2a;
		liftDecantLow   = GamepadLatest.dpadUp.value;
		liftDecantHigh  = GamepadLatest.leftBumper.value;
		liftHighSuspend = GamepadLatest.dpadRight.value;
		liftIDLE      	= GamepadLatest.dpadDown.value;
		decant			= GamepadLatest.x.value;
		suspend			= GamepadLatest.y.value;
		clipOption		= GamepadLatest.b.value && !lstGamepad2b;
		probe			= GamepadLatest.rightBumper.value	&& !lstGamepad2Right_bumper;

		lstGamepad2a=GamepadLatest.a.value;
		lstGamepad2b=GamepadLatest.b.value;
		lstGamepad2Right_bumper=GamepadLatest.rightBumper.value;
	}

	public static void printValues(){
		final TelemetryClient instance=TelemetryClient.getInstance();
		instance.changeData("intakeSamples",intakeSamples);
		instance.changeData("outtakeSamples",outtakeSamples);
		instance.changeData("flipArms",flipArms);
		instance.changeData("liftDecantLow",liftDecantLow);
		instance.changeData("liftDecantHigh",liftDecantHigh);
		instance.changeData("liftHighSuspend",liftHighSuspend);
		instance.changeData("liftIDLE",liftIDLE);
		instance.changeData("decant",decant);
		instance.changeData("suspend",suspend);
		instance.changeData("clipOption",clipOption);
		instance.changeData("probe",probe);

		GamepadLatest.output();
	}
}
