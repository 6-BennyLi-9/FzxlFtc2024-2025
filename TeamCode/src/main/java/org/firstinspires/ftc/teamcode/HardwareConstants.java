package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.actions.Actions;
import org.firstinspires.ftc.teamcode.actions.utils.SleepingAction;

public enum HardwareConstants {
	;
	public static DcMotorEx leftFront,leftRear,rightFront,rightRear;
	public static DcMotorEx lift;
	public static Servo     leftScale,rightScale,clip,place,leftArm,rightArm,intake;
	public static BNO055IMU imu;

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

		imu=hardwareMap.get(BNO055IMU.class,"imu");

		config();
	}

	/**
	 * 关于底盘的设定需要在 {@code Drive} 中修改
	 */
	public static void config(){
		lift.setDirection(DcMotorSimple.Direction.REVERSE);

		lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		final BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
		parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
		parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
		parameters.calibrationDataFile = "BNO055IMUCalibration.json";
		parameters.loggingEnabled = true;
		parameters.loggingTag = "IMU";
		parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
		//延时0.5秒，以确保imu正常工作
		Actions.runAction(new SleepingAction(500));
		imu.initialize(parameters);
	}
}
