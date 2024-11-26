package org.firstinspires.ftc.teamcode.util;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.action.Actions;
import org.firstinspires.ftc.teamcode.action.utils.SleepingAction;

public enum HardwareConstants {
	;
	public static DcMotorEx leftFront;
	public static DcMotorEx leftRear;
	public static DcMotorEx rightFront;
	public static DcMotorEx rightRear;
	public static DcMotorEx lift;
	public static Servo     leftScale;
	public static Servo rightScale;
	public static Servo clip;
	public static Servo place;
	public static Servo leftArm;
	public static Servo rightArm;
	public static Servo claw;
	public static Servo rotate;
	public static BNO055IMU   imu;
	public static TouchSensor liftTouch;

	public static void sync(@NonNull final HardwareMap hardwareMap,final boolean connectIMU){
		leftFront=hardwareMap.get(DcMotorEx.class,"leftFront");
		leftRear=hardwareMap.get(DcMotorEx.class,"leftRear");
		rightFront=hardwareMap.get(DcMotorEx.class,"rightFront");
		rightRear=hardwareMap.get(DcMotorEx.class,"rightRear");

		lift=hardwareMap.get(DcMotorEx.class,"lift");

		//收集
		leftScale=hardwareMap.get(Servo.class,"leftScale");
		rightScale=hardwareMap.get(Servo.class,"rightScale");
		claw=hardwareMap.get(Servo.class,"claw");
		rotate=hardwareMap.get(Servo.class,"rotate");

		//传递
		leftArm=hardwareMap.get(Servo.class,"leftArm");
		rightArm=hardwareMap.get(Servo.class,"rightArm");

		//放置
		clip=hardwareMap.get(Servo.class,"clip");
		place=hardwareMap.get(Servo.class,"place");

		if(connectIMU) {
			imu = hardwareMap.get(BNO055IMU.class, "imu");
		}
		liftTouch =hardwareMap.get(TouchSensor.class,"liftTouch");

		config(connectIMU);
	}

	/**
	 * 关于底盘的设定需要在 {@code Drive} 中修改
	 */
	public static void config(final boolean connectIMU){
		lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		lift.setDirection(DcMotorSimple.Direction.REVERSE);

		lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

		if(connectIMU) {
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

	/**
	 * 手动用
	 */
	public static void chassisConfig(){
		leftFront.setDirection(DcMotorSimple.Direction.REVERSE);   //F
		leftRear.setDirection(DcMotorSimple.Direction.REVERSE);    //R
		rightFront.setDirection(DcMotorSimple.Direction.FORWARD);  //F
		rightRear.setDirection(DcMotorSimple.Direction.FORWARD);   //R
	}
}
