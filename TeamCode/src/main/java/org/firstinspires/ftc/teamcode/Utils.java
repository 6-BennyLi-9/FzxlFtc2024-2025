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
import com.qualcomm.hardware.bosch.BNO055IMU.Parameters;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;

import java.util.concurrent.Callable;


@Config
public class Utils {

	public DcMotorEx leftFront;
	public DcMotorEx leftRear;
	public DcMotorEx rightFront;
	public DcMotorEx rightRear;
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
		init(hardwareMap, telemetry, false);
	}

	public void init(HardwareMap hardwareMap, Telemetry telemetry, boolean initIMU) {
		this.hardwareMap = hardwareMap;
		this.telemetry = telemetry;

		if(initIMU) {
			imuInit();
		}
	}

	public void imuInit() {
		imu = hardwareMap.get(BNO055IMU.class, "imu");

		Parameters param = new Parameters();
		param.angleUnit = BNO055IMU.AngleUnit.DEGREES;
		param.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
		param.calibrationDataFile = "BNO055Calibration.json";
		param.loggingEnabled = true;
		param.loggingTag = "IMU";
		param.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

		sleepForMS(500);

		imu.initialize(param);
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
	public static final int bufVal = 5;

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
	public strictfp void setPushPose(double position) {
		position = Math.max(Math.min(position, 0.82), 0.15);
		//0.9684931506849315 0.707/0.73
		//0.9863013698630137 0.72/0.73
		leftPush.setPosition(0.9684931506849315 - position * 0.9863013698630137);
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
				rearLiftToPosition(870);//挂样本 849
				break;
			default:
				throw new IllegalStateException(state.name());
		}
	}

	public void armOperationR(boolean y) {
		if (y) {
			clip.setPosition(HardwareDatabase3.clipOpen);  //打开
			arm.setPosition(HardwareDatabase3.armDownR);  //不翻转0.11
			upTurn.setPosition(HardwareDatabase3.upTurnDownR);
		} else {
			upTurn.setPosition(HardwareDatabase3.upTurnUpR);
			clip.setPosition(HardwareDatabase3.clipOn);  //夹住
			arm.setPosition(HardwareDatabase3.armUpR);  //翻转
		}
	}

	public void armOperationL(boolean d) {
		if (d) {
			clip.setPosition(HardwareDatabase3.clipOpen);  //打开
			arm.setPosition(HardwareDatabase3.armDownMiddle);
			upTurn.setPosition(HardwareDatabase3.upTurnDown);//不翻转0.11

		} else {
			clip.setPosition(HardwareDatabase3.clipOpen);  //打开
			arm.setPosition(HardwareDatabase3.armDownL);
			upTurn.setPosition(HardwareDatabase3.upTurnDown);//不翻转0.11
		}
	}

	/**
	 * @param y 如果为真，是左方。
	 */
	public void armOperation(boolean y) {
		if (y) {
			clip.setPosition(HardwareDatabase3.clipOn);  //夹住0.55
			arm.setPosition(HardwareDatabase3.armDownMiddle);  //不翻转
			upTurn.setPosition(HardwareDatabase3.upTurnDown);
			claw.setPosition(HardwareDatabase3.clawOpen);  //前夹子打开
		} else {
			clip.setPosition(HardwareDatabase3.clipOn);  //夹住
			arm.setPosition(HardwareDatabase3.armUpL); //翻转放块
			turn.setPosition(HardwareDatabase3.turnMiddle);
			upTurn.setPosition(HardwareDatabase3.upTurnUpL);
		}
	}

	/**
	 * @param s 如果为真，打开
	 */
	public void clipOperation(boolean s) {
		this.clip.setPosition(s ? HardwareDatabase3.clipOpen : HardwareDatabase3.clipOn); //开/关
	}

	/**
	 * @param s 如果为真，打开
	 */
	public void clawOperation(boolean s) {
		this.claw.setPosition(s ? HardwareDatabase3.clawOpen : HardwareDatabase3.clawOn); //开/关
	}

	public void claw_rotate_rst(boolean x) {
		if (x) {
			claw.setPosition(HardwareDatabase3.clawOpen);  //打开0.26
			turn.setPosition(HardwareDatabase3.turnMiddle);   //翻下去
			rotate.setPosition(HardwareDatabase3.rotateOn);  //转正
		} else {
			claw.setPosition(HardwareDatabase3.clawOpen);  //打开0.26
			turn.setPosition(HardwareDatabase3.turnDown);   //翻下去
			rotate.setPosition(HardwareDatabase3.rotateOn);  //转正
		}
	}

	public void claw_rotate_rst1(boolean x) {
		if (x) {
			claw.setPosition(HardwareDatabase3.clawOpen);  //打开0.26
			turn.setPosition(HardwareDatabase3.turnMiddle);   //翻下去
			rotate.setPosition(HardwareDatabase3.rotateOn);  //转正
		} else {
			rotate.setPosition(0.51);  //偏转角度
			claw.setPosition(HardwareDatabase3.clawOpen);  //打开0.26
			turn.setPosition(HardwareDatabase3.turnDown);   //翻下去

		}
	}

	public void claw_rotate(boolean m) {
		if (m) {
			claw.setPosition(HardwareDatabase3.clawOn);  //夹住
			turn.setPosition(HardwareDatabase3.turnUp);   //翻上去
			rotate.setPosition(HardwareDatabase3.rotateOn); //转正0.1, 0.83
			clip.setPosition(HardwareDatabase3.clipOpen);   //夹子打开
		} else {
			claw.setPosition(HardwareDatabase3.clawOpen);  //打开0.26
			turn.setPosition(HardwareDatabase3.turnUp);   //翻上去
			rotate.setPosition(HardwareDatabase3.rotateOn);  //旋转90度
		}
	}

	//IMU调整
	public static final double allowErr = 5;

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
