package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public enum HardwareConstants {
	;
	public static DcMotorEx leftFront,leftRear,rightFront,rightRear;
	public static DcMotorEx lift;
	public static Servo     leftScale,rightScale,clip,place,leftArm,rightArm,intake;

	public static void sync(@NonNull final HardwareMap hardwareMap){
		leftFront=hardwareMap.get(DcMotorEx.class,"leftFront");
		leftRear=hardwareMap.get(DcMotorEx.class,"leftRear");
		rightFront=hardwareMap.get(DcMotorEx.class,"rightFront");
		rightRear=hardwareMap.get(DcMotorEx.class,"rightRear");

		lift=hardwareMap.get(DcMotorEx.class,"lift");

		//收集
		leftScale=hardwareMap.get(Servo.class,"leftScale");
		rightScale=hardwareMap.get(Servo.class,"rightScale");
		intake=hardwareMap.get(Servo.class,"intake");

		//传递
		leftArm=hardwareMap.get(Servo.class,"leftArm");
		rightArm=hardwareMap.get(Servo.class,"rightArm");

		//放置
		clip=hardwareMap.get(Servo.class,"clip");
		place=hardwareMap.get(Servo.class,"place");

		config();
	}

	/**
	 * 关于底盘的设定需要在 {@code Drive} 中修改
	 */
	public static void config(){
		lift.setDirection(DcMotorSimple.Direction.REVERSE);

		lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
	}
}
