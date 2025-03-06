package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

public final class HardwareDatabase {
	public static DcMotorEx   leftFront;
	public static DcMotorEx   leftRear;
	public static DcMotorEx   rightFront;
	public static DcMotorEx   rightRear;
	public static DcMotorEx   rightLift;
	public static DcMotorEx   leftLift;
	public static Servo       leftScale;
	public static Servo       rightScale;
	/**
	 * 用于悬挂样本
	 */
	public static Servo       clip;
	public static Servo       place;
	public static Servo       leftArm;
	public static Servo       rightArm;
	/**
	 * 用于夹取样本
	 */
	public static Servo       claw;
	public static Servo       rotate;
	public static BNO055IMU   imu;
	public static TouchSensor liftTouch;

	private static HardwareMap hardwareMap;

	public static void sync(@NonNull final HardwareMap hardwareMap, final boolean connectIMU) {
		HardwareDatabase.hardwareMap = hardwareMap;

		leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
		leftRear = hardwareMap.get(DcMotorEx.class, "leftRear");
		rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
		rightRear = hardwareMap.get(DcMotorEx.class, "rightRear");

		leftLift = hardwareMap.get(DcMotorEx.class, "leftLift");
		rightLift = hardwareMap.get(DcMotorEx.class, "rightLift");

		//收集
		leftScale = hardwareMap.get(Servo.class, "leftScale");
		rightScale = hardwareMap.get(Servo.class, "rightScale");
		claw = hardwareMap.get(Servo.class, "claw");
		rotate = hardwareMap.get(Servo.class, "rotate");

		//传递
		leftArm = hardwareMap.get(Servo.class, "leftArm");
		rightArm = hardwareMap.get(Servo.class, "rightArm");

		//放置
		clip = hardwareMap.get(Servo.class, "clip");
		place = hardwareMap.get(Servo.class, "place");

		liftTouch = hardwareMap.get(TouchSensor.class, "liftTouch");

		if (connectIMU) {
			syncIMU();
		}
		config(connectIMU);
	}

	/// 关于底盘的设定需要在 `Drive` 中修改
	public static void config(final boolean connectIMU) {
		leftLift.setZeroPowerBehavior(BRAKE);
		rightLift.setZeroPowerBehavior(BRAKE);
		leftFront.setZeroPowerBehavior(BRAKE);
		leftRear.setZeroPowerBehavior(BRAKE);
		rightFront.setZeroPowerBehavior(BRAKE);
		rightRear.setZeroPowerBehavior(BRAKE);

		leftLift.setDirection(FORWARD);
		rightLift.setDirection(REVERSE);

		leftLift.setMode(STOP_AND_RESET_ENCODER);
		leftLift.setMode(RUN_WITHOUT_ENCODER);
		rightLift.setMode(STOP_AND_RESET_ENCODER);
		rightLift.setMode(RUN_WITHOUT_ENCODER);

		if (connectIMU) {
			final BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
			parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
			parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
			parameters.calibrationDataFile = "BNO055IMUCalibration.json";
			parameters.loggingEnabled = true;
			parameters.loggingTag = "IMU";
			parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
			//延时0.5秒，以确保imu正常工作
			Local.sleep(500);
			imu.initialize(parameters);
		}
	}

	public static void syncIMU() {
		imu = hardwareMap.get(BNO055IMU.class, "imu");
	}

	/// 手动用
	public static void chassisConfig() {
		leftFront.setDirection(REVERSE);   //F
		leftRear.setDirection(REVERSE);    //R
		rightFront.setDirection(FORWARD);  //F
		rightRear.setDirection(FORWARD);   //R
	}
}
