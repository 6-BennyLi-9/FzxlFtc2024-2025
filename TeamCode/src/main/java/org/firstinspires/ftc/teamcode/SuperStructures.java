package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static org.firstinspires.ftc.teamcode.HardwareParams.clipOn;
import static org.firstinspires.ftc.teamcode.HardwareParams.clipOpen;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class SuperStructures {
	public    HardwareMap hardwareMap;
	public    Telemetry   telemetry;
	public    Gamepad     gamepad2;
	public    DcMotorEx   leftLift;
	public    DcMotorEx   rightLift;
	public    Servo       arm;                        //后电梯上摆臂
	public    Servo       clip;                        //后电梯上的夹取  前
	public    Servo       turn;                        //前电梯上的翻转舵机
	public    Servo       claw;                    //自动紫色像素释放 左
	public    Servo       rotate;              //盒子像素卡扣  后
	public    TouchSensor touch;
	public    Servo       upTurn;              //后电梯上摆臂
	public    Servo       leftPush;            //后电梯上的夹取  前
	public    Servo       rightPush;           //前电梯上的翻转舵机
	protected boolean     lift_up_event;
	//用一个按键控制不同的状态
	protected int         armPutEvent         = 0;
	protected boolean     keyFlag_arm         = false;
	protected int         clawPutEvent        = 0;
	protected boolean     keyFlag_claw        = false;
	protected int         clipPutEvent        = 0;
	protected boolean     keyFlag_clip        = false;
	//电梯的抬升，为了防止电机高速运转带来的encoder的值的快速变化，当高速抬升到固定的encoder值时，
	// 以匀速低速的形式来实现前抬升的前100转和下降的后100转以低速
	protected int         right_encoder_value = 0;

	public void init(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad2) {
		this.hardwareMap = hardwareMap;
		this.telemetry = telemetry;
		this.gamepad2 = gamepad2;
	}

	public void motorInit(String leftLift, String rightLift, String touch) {
		this.leftLift = hardwareMap.get(DcMotorEx.class, leftLift);
		this.rightLift = hardwareMap.get(DcMotorEx.class, rightLift);
		this.touch = hardwareMap.get(TouchSensor.class, touch);

		this.leftLift.setDirection(DcMotorSimple.Direction.FORWARD);
		this.rightLift.setDirection(DcMotorSimple.Direction.FORWARD);

		this.leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		this.rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		this.leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		this.leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		this.rightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		this.rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

		while (! this.touch.isPressed()) {
			this.leftLift.setPower(- 0.3);
			this.rightLift.setPower(- 0.3);
		}
		this.leftLift.setPower(0);
		this.rightLift.setPower(0);

		this.leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		this.leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		this.rightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		this.rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
	}

	public void servoInit(String arm, String turn, String clip, String rotate, String claw, String upTurn, String leftPush, String rightPush) {
		this.arm = hardwareMap.get(Servo.class, arm);
		this.turn = hardwareMap.get(Servo.class, turn);
		this.clip = hardwareMap.get(Servo.class, clip);
		this.rotate = hardwareMap.get(Servo.class, rotate);
		this.claw = hardwareMap.get(Servo.class, claw);
		this.upTurn = hardwareMap.get(Servo.class, upTurn);
		this.leftPush = hardwareMap.get(Servo.class, leftPush);
		this.rightPush = hardwareMap.get(Servo.class, rightPush);

		setPushPose(0.82);
	}

	public strictfp void setPushPose(double position) {
		position = Math.max(Math.min(position, 0.82), 0.15);
		leftPush.setPosition(0.8484931506849315 - 0.9863013698630136 * position);
		rightPush.setPosition(position);
	}
	
	public abstract void optionThroughGamePad();

	protected abstract void armOperation(boolean key);

	protected abstract void clawOperation(boolean key);

	protected void inlineClipOperation(boolean key) {
		telemetry.addData("clip:", "%d", clipPutEvent);
		if (key) {
			if (! keyFlag_clip) {
				keyFlag_clip = true;
				if (clipPutEvent < 1)  //原来值是3
				{
					clipPutEvent++;
				} else {
					clipPutEvent = 0;
				}
			}
			switch (clipPutEvent) {
				case 0:
					clip.setPosition(clipOpen);  //释放
					break;
				case 1:
					clip.setPosition(clipOn);  //夹住
					break;
			}
		} else {
			keyFlag_clip = false;
		}
	}

	protected abstract void inlineArmOperation(boolean y);

	protected abstract void inlineClawOpenOperation();

	protected void inlineClipOpenOperation() {
		clip.setPosition(clipOpen);  //打开
	}

	protected void setLiftPosition(int val) {
		right_encoder_value = val;
	}

	protected void liftPositionUpdate() {
		final int max_position = 2500;//原1000
		final int bufVal       = 10;
		if (right_encoder_value < max_position && right_encoder_value > 5) {
			leftLift.setTargetPosition(right_encoder_value);
			rightLift.setTargetPosition(right_encoder_value);
			leftLift.setTargetPositionTolerance(bufVal);
			rightLift.setTargetPositionTolerance(bufVal);
			leftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
			rightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
			leftLift.setPower(1);
			rightLift.setPower(1);
		} else if (right_encoder_value == 0) {
			leftLift.setMode(RUN_WITHOUT_ENCODER);
			rightLift.setMode(RUN_WITHOUT_ENCODER);
			if (touch.isPressed()) {
				leftLift.setPower(0);
				rightLift.setPower(0);
				leftLift.setMode(STOP_AND_RESET_ENCODER);
				leftLift.setMode(RUN_WITHOUT_ENCODER);
				rightLift.setMode(STOP_AND_RESET_ENCODER);
				rightLift.setMode(RUN_WITHOUT_ENCODER);
			} else {
				leftLift.setPower(- 1.0);
				rightLift.setPower(- 1.0);
			}
		}
	}
}
