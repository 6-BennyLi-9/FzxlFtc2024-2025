package org.firstinspires.ftc.teamcode;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 */
@Config
public class SuperStructuresR {
	public HardwareMap hardwareMap;
	public Telemetry   telemetry;
	public Gamepad     gamepad2;

	public DcMotorEx leftLift;
	public DcMotorEx rightLift;

	public Servo arm;    //后电梯上摆臂
	public Servo clip;   //后电梯上的夹取  前
	public Servo turn;   //前电梯上的翻转舵机
	public Servo claw;   //自动紫色像素释放 左


	public Servo       rotate;     //盒子像素卡扣  后
	public TouchSensor touch;
	public Servo       upTurn;     //后电梯上摆臂

	public ServoSmoothController leftPushController;//后电梯上的夹取  前
	public ServoSmoothController rightPushController;//前电梯上的翻转舵机

	public static final double turnUp     = 0.07; //0.31
	public static final double turnMiddle = 0.71;
	public static final double turnDown   = 0.87;
	public static final double rotateOn   = 0.49;
	public static final double clawOn   = 0.63;
	public static final double clawOpen = 0.32;
	public static final double clipOn   = 0.79; //0.78
	public static final double clipOpen = 0.52;
	public static final double armPut   = 0.90;  //翻转去夹上提0.56,0.84，0.93
	public static final double armGet   = 0.20;  //0.16,挂边框0.14翻转去挂0.20
	public static final double upTurnGet = 0.8;  //翻转去夹0.78,挂边框0.58
	public static final double upTurnPut = 0.16;  //翻转去挂0.05,上提0.67，0.16

	public void init(HardwareMap h, Telemetry t, Gamepad g2) {
		hardwareMap = h;
		telemetry = t;
		gamepad2 = g2;
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

		leftPushController = new ServoSmoothController(hardwareMap.get(Servo.class, leftPush));
		rightPushController = new ServoSmoothController(hardwareMap.get(Servo.class, rightPush));

		setPushPose(0.82);
	}

	public strictfp void setPushPose(double position) {
		position = Math.max(Math.min(position, 0.82), 0.15);
		//0.9684931506849315 0.707/0.73
		//0.9863013698630137 0.72/0.73

		leftPushController.setTarget(0.9684931506849315 - position * 0.9863013698630137);
		rightPushController.setTarget(position);

		leftPushController.update();
		rightPushController.update();
	}

	private boolean lift_up_event;

	public void optionThroughGamePad() {
		if (gamepad2.right_bumper) {
			arm.setPosition(0.88);

			if (! lift_up_event) {
				if (right_encoder_value == 1620) {
					setLiftPosition(2300);//中间位置
				} else {
					setLiftPosition(1620);//中间位置
				}
				lift_up_event = true;
			}
		} else {
			lift_up_event = false;
		}

		if (gamepad2.left_bumper) {
			//clipOperation1(true);
			inlineArmOperation(false);
			arm.setPosition(0.88);
			setLiftPosition(172);//初始位置
		}

		if (gamepad2.dpad_right) {
			inlineClawOpenOperation();
			setLiftPosition(875);   //挂样本883，910, 852, 849, 872
			inlineArmOperation(true);
		}
		if (gamepad2.dpad_left) {
			//clipOperation1(true);
			inlineArmOperation(false);
			arm.setPosition(0.88);
			setLiftPosition(172);//初始位置
		}
		if (gamepad2.dpad_down) {
			//clipOperation1(true);
			inlineArmOperation(false);
			setLiftPosition(0);//初始位置
		}

		setPushPose(rightPushController.getTarget() + gamepad2.left_stick_y * 0.035);

		liftPositionUpdate();
		rotate.setPosition(rotate.getPosition() + 0.03 * (gamepad2.left_trigger - gamepad2.right_trigger));

		clipOperation(gamepad2.a);
		clawOperation(gamepad2.b);
		armOperation(gamepad2.x);
	}

	//用一个按键控制不同的状态
	int     armPutEvent = 0;
	boolean keyFlag_arm = false;

	private void armOperation(boolean key) {
		telemetry.addData("arm:", "%d", armPutEvent);
		telemetry.addData("armPosition:", "%f", arm.getPosition());
		if (key) {
			if (! keyFlag_arm) {
				keyFlag_arm = true;
				if (armPutEvent < 1) {//3
					armPutEvent++;
				} else {
					armPutEvent = 0;
				}
			}
			switch (armPutEvent) {

				case 0:
					//夹住
					arm.setPosition(armGet);  //翻转挂矿石
					upTurn.setPosition(upTurnGet);
					break;
				case 1:
					//夹住
					//arm.setPosition(armPut);  //翻转挂矿石
					upTurn.setPosition(upTurnGet);
					clip.setPosition(clipOpen);
					break;

			}
		} else {
			keyFlag_arm = false;
		}
	}

	int     clawPutEvent = 0;
	boolean keyFlag_claw = false;

	private void clawOperation(boolean key) {
		telemetry.addData("claw:", "%d", clawPutEvent);
		if (key) {
			if (! keyFlag_claw) {
				keyFlag_claw = true;
				if (clawPutEvent < 3)  //原来值是3
				{
					clawPutEvent++;
				} else {
					clawPutEvent = 0;
				}
			}
			switch (clawPutEvent) {
				case 0:
					claw.setPosition(clawOn);  //扣住
					turn.setPosition(turnUp);  //翻转上去
					rotate.setPosition(rotateOn); //保持水平0.63，0
					break;
				case 1:
					claw.setPosition(clawOpen);  //打开
					turn.setPosition(turnMiddle);  //翻转下去
					rotate.setPosition(rotateOn); //保持水平0.1,0.83
					break;
				case 2:
					claw.setPosition(clawOpen);  //打开
					turn.setPosition(turnDown);  //翻转下去
					rotate.setPosition(rotateOn); //保持水平0.1,0.83
					break;
				case 3:
					claw.setPosition(clawOn);  //夹住
					turn.setPosition(turnDown);  //翻转下去
					break;
			}
		} else {
			keyFlag_claw = false;
		}
	}

	int     clipPutEvent = 0;
	boolean keyFlag_clip = false;

	private void clipOperation(boolean key) {
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
					upTurn.setPosition(upTurnGet);
					break;
				case 1:
					//夹住
					clip.setPosition(clipOn);
					break;

			}
		} else {
			keyFlag_clip = false;
		}
	}

	private void inlineArmOperation(boolean y) {//TODO
		if (y) {
			clip.setPosition(clipOn);  //夹住
			arm.setPosition(armPut);  //翻转挂矿石
			upTurn.setPosition(upTurnPut);
			claw.setPosition(clawOpen);
		} else {
			clip.setPosition(clipOpen);  //打开
			arm.setPosition(armGet);  //中间等待位置
			upTurn.setPosition(upTurnGet);
		}
	}


	private void inlineClawOpenOperation() {//TODO
		claw.setPosition(clawOpen);   //打开
		turn.setPosition(turnUp);  //翻转下去
		rotate.setPosition(rotateOn); //保持水平0.1,0.83
	}

	//电梯的抬升，为了防止电机高速运转带来的encoder的值的快速变化，当高速抬升到固定的encoder值时，
	// 以匀速低速的形式来实现前抬升的前100转和下降的后100转以低速
	int right_encoder_value = 0;

	private void setLiftPosition(int val) {
		right_encoder_value = val;
	}

	private void liftPositionUpdate() {
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
			leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
			rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
			if (touch.isPressed())//40,210
			{
				leftLift.setPower(0);
				rightLift.setPower(0);
				leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
				leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
				rightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
				rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
			} else {
				leftLift.setPower(- 1.0);
				rightLift.setPower(- 1.0);
			}
		}
	}
}

