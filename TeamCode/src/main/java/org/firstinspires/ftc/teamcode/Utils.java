package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.util.concurrent.Callable;


@Config
public class Utils {
	public static double    turnUp        = 0.06; //0.31
	public static double    turnMiddle    = 0.71;
	public static double    turnDown      = 0.87;
	public static double    rotateOn      = 0.49;
	public static double    clawOn        = 0.63;
	public static double    clawOpen      = 0.32;
	public static double    clipOn        = 0.81;
	public static double    clipOpen      = 0.49;
	public static double    armUpL        = 0.39;
	public static double    armDownMiddle = 0.83;
	public static double    armDownL      = 0.88;
	public static double    armUpR        = 0.86;
	public static double    armDownR      = 0.16;
	public static double    upTurnUpL     = 0.23;  //翻转去夹0.16
	public static double    upTurnDown    = 0.80;  //翻转去挂
	public static double    upTurnUpR     = 0.05;  //翻转去夹0.16
	public static double    upTurnDownR   = 0.92;  //翻转去挂
	public static double    pushOut       = 0.27;   //翻转去夹0.16
	public static double    pushIn        = 0.80;   //翻转去挂
	public static DcMotorEx leftFront;
	public static DcMotorEx leftRear;
	public static DcMotorEx rightFront;
	public static DcMotorEx rightRear;

	public DcMotorEx leftLift;
	public DcMotorEx rightLift;

	public Servo arm;	//后电梯摆臂
	public Servo rotate;//前电梯上旋转舵机
	public Servo clip;  //后电梯夹子
	public Servo claw;  //前电梯夹子
	public Servo turn;  //前电梯上，上下翻转

	public TouchSensor touch;
	public Servo       upTurn;         //后电梯上摆臂
	public Servo       leftPush;       //后电梯上的夹取  前
	public Servo       rightPush;
	public BNO055IMU   imu;

	public HardwareMap hardwareMap;
	public Telemetry   telemetry;

	public void init(HardwareMap hardwareMap, Telemetry telemetry) {
		this.hardwareMap = hardwareMap;
		this.telemetry = telemetry;

		imuInit();
	}

	public void imuInit() {
		imu = hardwareMap.get(BNO055IMU.class, "imu");

		BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();//不可更改
		parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
		parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
		parameters.calibrationDataFile = "BNO055Calibration.json";
		parameters.loggingEnabled = true;
		parameters.loggingTag = "IMU";
		parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

		sleepForMS(500);

		imu.initialize(parameters);
	}

	public void liftMotorInit(String leftLift, String rightLift, String touch) {
		this.leftLift = hardwareMap.get(DcMotorEx.class, leftLift);
		this.rightLift = hardwareMap.get(DcMotorEx.class, rightLift);
		this.touch = hardwareMap.get(TouchSensor.class, touch);

		this.leftLift.setDirection(FORWARD);
		this.rightLift.setDirection(FORWARD);

		this.leftLift.setZeroPowerBehavior(BRAKE);
		this.rightLift.setZeroPowerBehavior(BRAKE);

		this.leftLift.setMode(STOP_AND_RESET_ENCODER);
		this.leftLift.setMode(RUN_WITHOUT_ENCODER);
		this.rightLift.setMode(STOP_AND_RESET_ENCODER);
		this.rightLift.setMode(RUN_WITHOUT_ENCODER);

		while (! this.touch.isPressed()) {
			this.leftLift.setPower(- 0.3);//根据电机的正负设置power
			this.rightLift.setPower(- 0.3);//根据电机的正负设置power
		}

		this.leftLift.setPower(0);
		this.rightLift.setPower(0);

		this.leftLift.setMode(STOP_AND_RESET_ENCODER);
		this.leftLift.setMode(RUN_WITHOUT_ENCODER);
		this.rightLift.setMode(STOP_AND_RESET_ENCODER);
		this.rightLift.setMode(RUN_WITHOUT_ENCODER);
	}

	public void motorInit() {
		leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
		leftRear = hardwareMap.get(DcMotorEx.class, "leftRear");
		rightRear = hardwareMap.get(DcMotorEx.class, "rightRear");
		rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
	}

	public void servoInit(String arm, String clip, String rotate, String turn, String claw, String upTurn, String leftPush, String rightPush) {
		this.arm = hardwareMap.get(Servo.class, arm);
		this.turn = hardwareMap.get(Servo.class, turn);
		this.clip = hardwareMap.get(Servo.class, clip);
		this.rotate = hardwareMap.get(Servo.class, rotate);
		this.claw = hardwareMap.get(Servo.class, claw);
		this.upTurn = hardwareMap.get(Servo.class, upTurn);
		this.leftPush = hardwareMap.get(Servo.class, leftPush);
		this.rightPush = hardwareMap.get(Servo.class, rightPush);
	}

	public void sleepForMS(int time) {
		try {
			Thread.sleep(time);//单位：毫秒
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}


	//后电梯
	public static int bufVal = 5;

	public void rearLiftToPosition(int rear_encoder_value) {
		leftLift.setTargetPosition(rear_encoder_value);
		rightLift.setTargetPosition(rear_encoder_value);
		leftLift.setTargetPositionTolerance(bufVal);
		rightLift.setTargetPositionTolerance(bufVal);
		leftLift.setMode(RUN_TO_POSITION);
		rightLift.setMode(RUN_TO_POSITION);
		leftLift.setPower(1);
		rightLift.setPower(1);
	}

	/**
	 * @param position 0.15~0.82
	 */
	public void setPushPose(double position) {
		position = Math.max(Math.min(position, 0.82), 0.15);
		leftPush.setPosition(1 - position);
		rightPush.setPosition(position);
	}


	public void rearLiftRst() {
		leftLift.setMode(RUN_WITHOUT_ENCODER);
		rightLift.setMode(RUN_WITHOUT_ENCODER);

		if (leftLift.getCurrentPosition() > 210) {
			leftLift.setPower(- 1.0);
			rightLift.setPower(- 1.0);
		} else if (leftLift.getCurrentPosition() > 20) {
			leftLift.setPower(- 0.3);
			rightLift.setPower(- 0.3);
		} else if (touch.isPressed())//40,210
		{
			leftLift.setPower(0);
			rightLift.setPower(0);
			leftLift.setMode(STOP_AND_RESET_ENCODER);
			leftLift.setMode(RUN_WITHOUT_ENCODER);
			rightLift.setMode(STOP_AND_RESET_ENCODER);
			rightLift.setMode(RUN_WITHOUT_ENCODER);
		}
	}

	public void setRearLiftPosition(@NonNull RearLiftLocation state) {
		switch (state) {
			case down:
				rearLiftRst();
				break;
			case up:
				rearLiftToPosition(2350);//放篮子，原2300
				break;
			case middle:
				rearLiftToPosition(849);//挂样本
				break;
			default:
				throw new IllegalStateException(state.name());
		}
	}

	public void armOperationR(boolean y) {
		if (y) {
			clip.setPosition(clipOpen);  //打开
			arm.setPosition(armDownR);  //不翻转0.11
			upTurn.setPosition(upTurnDownR);
		} else {
			upTurn.setPosition(upTurnUpR);
			clip.setPosition(clipOn);  //夹住
			arm.setPosition(armUpR);  //翻转
		}
	}

	public void armOperationL(boolean d) {
		if (d) {
			clip.setPosition(clipOpen);  //打开
			arm.setPosition(armDownMiddle);
			upTurn.setPosition(upTurnDown);//不翻转0.11

		} else {
			clip.setPosition(clipOpen);  //打开
			arm.setPosition(armDownL);
			upTurn.setPosition(upTurnDown);//不翻转0.11
		}
	}

	/**
	 * @param y 如果为真，是左方。
	 */
	public void armOperation(boolean y) {
		if (y) {
			clip.setPosition(clipOn);  //夹住0.55
			arm.setPosition(armDownMiddle);  //不翻转
			upTurn.setPosition(upTurnDown);
			claw.setPosition(clawOpen);  //前夹子打开
		} else {
			clip.setPosition(clipOn);  //夹住
			arm.setPosition(armUpL); //翻转放块
			turn.setPosition(turnMiddle);
			upTurn.setPosition(upTurnUpL);
		}
	}

	/**
	 * @param s 如果为真，打开
	 */
	public void clipOperation(boolean s) {
		this.clip.setPosition(s ? clipOpen : clipOn); //开/关
	}

	/**
	 * @param s 如果为真，打开
	 */
	public void clawOperation(boolean s) {
		this.claw.setPosition(s ? clawOpen : clawOn); //开/关
	}

	public void claw_rotate_rst(boolean x) {
		if (x) {
			claw.setPosition(clawOpen);  //打开0.26
			turn.setPosition(turnMiddle);   //翻下去
			rotate.setPosition(rotateOn);  //转正
		} else {
			claw.setPosition(clawOpen);  //打开0.26
			turn.setPosition(turnDown);   //翻下去
			rotate.setPosition(rotateOn);  //转正
		}
	}

	public void claw_rotate_rst1(boolean x) {
		if (x) {
			claw.setPosition(clawOpen);  //打开0.26
			turn.setPosition(turnMiddle);   //翻下去
			rotate.setPosition(rotateOn);  //转正
		} else {
			rotate.setPosition(0.51);  //偏转角度
			claw.setPosition(clawOpen);  //打开0.26
			turn.setPosition(turnDown);   //翻下去

		}
	}

	public void claw_rotate(boolean m) {
		if (m) {
			claw.setPosition(clawOn);  //夹住
			turn.setPosition(turnUp);   //翻上去
			rotate.setPosition(rotateOn); //转正0.1, 0.83
			clip.setPosition(clipOpen);   //夹子打开
		} else {
			claw.setPosition(clawOpen);  //打开0.26
			turn.setPosition(turnUp);   //翻上去
			rotate.setPosition(rotateOn);  //旋转90度
		}
	}

	//IMU调整
	public static double allowErr = 5;

	public void angleCalibration(final double target, @NonNull final Pose2d poseEst, @NonNull SampleMecanumDrive drive) {
		runAction(() -> {
			final double ang = imu.getAngularOrientation().firstAngle;

			TelemetryPacket p = new TelemetryPacket();
			p.put("ang", ang);
			p.put("err", Math.abs(target - ang));
			FtcDashboard.getInstance().sendTelemetryPacket(p);

			if (Math.abs(target - ang) < Math.abs(360 - target + ang)) {
				if (ang > target + allowErr) {
					simpleDrive(- 0.5);
					return true;
				} else if (ang < target - allowErr) {
					simpleDrive(0.5);
					return true;
				}
			} else {
				if (ang > target + allowErr) {
					simpleDrive(0.5);
					return true;
				} else if (ang < target - allowErr) {
					simpleDrive(- 0.5);
					return true;
				}
			}
			simpleDrive(0);
			return false;
		});
		drive.setPoseEstimate(poseEst);
	}

	protected void simpleDrive(double angle) {
		leftFront.setPower(- angle);
		leftRear.setPower(- angle);
		rightFront.setPower(angle);
		rightRear.setPower(angle);
	}

	protected void runAction(Callable <Boolean> c) {
		while (true) {
			try {
				if (! c.call()) break;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
