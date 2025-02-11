package org.firstinspires.ftc.teamcode;


import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.LinkedList;
import java.util.List;

/** @noinspection FieldCanBeLocal*/
public class SuperStructuresL {
	private HardwareMap hardwareMap;
	private Telemetry telemetry;
	private Gamepad gamepad1, gamepad2;


	private DcMotorEx leftLift;
	private DcMotorEx rightLift;

	private Servo arm;         //后电梯上摆臂
	private Servo clip;        //后电梯上的夹取  前
	private Servo turn;         //前电梯上的翻转舵机
	private Servo claw;   //自动紫色像素释放 左


	private Servo rotate;     //盒子像素卡扣  后
	private TouchSensor touch;
	private Servo upTurn;         //后电梯上摆臂
	private Servo leftPush;        //后电梯上的夹取  前
	private Servo rightPush;         //前电梯上的翻转舵机

	public static double turnUp = 0.06; //0.31
	public static double turnMiddle = 0.71;
	public static double turnDown = 0.87;
	public static double rotateOn = 0.49;
	public static double clawOn = 0.64;
	public static double clawOpen = 0.32;
	public static double clipOn = 0.81;
	public static double clipOpen = 0.49;
	public static double armDown = 0.89;  //翻转去夹0.16 0.89
	public static double armMiddle = 0.83;  //翻转去挂
	public static double armUp = 0.43;  //翻转去挂
	public static double upTurnUp = 0.24;  //翻转去夹0.16
	public static double upTurnDown = 0.91;  //翻转去挂
	public List<ButtonLock> buttons = new LinkedList<>();

	public void init(HardwareMap h, Telemetry t, Gamepad g1, Gamepad g2) {
		hardwareMap = h;
		telemetry = t;
		gamepad1 = g1;
		gamepad2 = g2;
	}

	public void motorInit(String leftLift, String rightLift, String touch) {
		this.leftLift = hardwareMap.get(DcMotorEx.class, leftLift);
		this.rightLift = hardwareMap.get(DcMotorEx.class, rightLift);
		this.touch = hardwareMap.touchSensor.get(touch);

		this.leftLift.setDirection(FORWARD);
		this.rightLift.setDirection(FORWARD);

		this.leftLift.setZeroPowerBehavior(BRAKE);
		this.rightLift.setZeroPowerBehavior(BRAKE);

		this.leftLift.setMode(STOP_AND_RESET_ENCODER);
		this.leftLift.setMode(RUN_WITHOUT_ENCODER);
		this.rightLift.setMode(STOP_AND_RESET_ENCODER);
		this.rightLift.setMode(RUN_WITHOUT_ENCODER);


		while (!this.touch.isPressed()) {
			this.leftLift.setPower(-0.3);//根据电机的正负设置power
			this.rightLift.setPower(-0.3);//根据电机的正负设置power
		}
		this.leftLift.setPower(0);
		this.rightLift.setPower(0);

		this.leftLift.setMode(STOP_AND_RESET_ENCODER);
		this.leftLift.setMode(RUN_WITHOUT_ENCODER);
		this.rightLift.setMode(STOP_AND_RESET_ENCODER);
		this.rightLift.setMode(RUN_WITHOUT_ENCODER);
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
	}

	public void setPushPose(double position) {// FIXME: 2025/2/11 憋
		position = Math.max(Math.min(position, 0.82), 0.15);
		leftPush.setPosition(1 - position);
		rightPush.setPosition(position);
	}

	public void optionThroughGamePad() {

		if (gamepad2.left_bumper) {
			setLiftPosition(2300);//中间位置
			arm.setPosition(0.88);
		}
		if (gamepad2.dpad_up) {
			setLiftPosition(1620);//中间位置
			arm.setPosition(0.88);
		}
		if (gamepad2.dpad_left) {
			//clipOperation1(true);
			armOperation1(false);
			arm.setPosition(0.88);
			setLiftPosition(0);//初始位置
		}
		if (gamepad2.dpad_right) {
			clawOperation1(true);
			setLiftPosition(2300);//挂高杆2195
			armOperation1(true);
		}
		if (gamepad2.dpad_down) {
			clipOperation1(true);
			armOperation1(false);
			setLiftPosition(0);//初始位置
		}


		setPushPose(rightPush.getPosition() + gamepad2.left_stick_y * 0.035);

		LiftPositionUpdate();
		if (gamepad2.right_bumper) {
			rotate.setPosition(rotate.getPosition() + 0.02);
		}

		clipOperation(gamepad2.a);
		clawOperation(gamepad2.b);
		armOperation(gamepad2.x);
	}

	//用一个按键控制不同的状态
	int armPutEvent = 0;
	boolean keyFlag_arm = false;

	private void armOperation(boolean key) {
		telemetry.addData("arm:", "%d", armPutEvent);
		telemetry.addData("armPosition:", "%f", arm.getPosition());
		if (key) {
			if (!keyFlag_arm) {
				keyFlag_arm = true;
				if (armPutEvent < 1)  //原来值是3
					armPutEvent++;
				else armPutEvent = 0;
			}
			switch (armPutEvent) {
				case 0:
					clip.setPosition(clipOn);
					break;
				case 1:
					arm.setPosition(armDown);  //打开
					clip.setPosition(clipOpen);
					break;
			}
		} else keyFlag_arm = false;
	}

	int clawPutEvent = 0;
	boolean keyFlag_claw = false;

	private void clawOperation(boolean key) {
		telemetry.addData("claw:", "%d", clawPutEvent);
		if (key) {
			if (!keyFlag_claw) {
				keyFlag_claw = true;
				if (clawPutEvent < 3)  //原来值是3
					clawPutEvent++;
				else clawPutEvent = 0;
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
		} else keyFlag_claw = false;
	}

	int clipPutEvent = 0;
	boolean keyFlag_clip = false;

	private void clipOperation(boolean key) {
		telemetry.addData("clip:", "%d", clipPutEvent);
		if (key) {
			if (!keyFlag_clip) {
				keyFlag_clip = true;
				if (clipPutEvent < 1)  //原来值是3
					clipPutEvent++;
				else clipPutEvent = 0;
			}
			switch (clipPutEvent) {
				case 0:

					clip.setPosition(clipOpen);  //释放

					break;
				case 1:
					clip.setPosition(clipOn);  //夹住

					break;

			}
		} else keyFlag_clip = false;
	}

	private void armOperation1(boolean y) {
		if (y) {
			clip.setPosition(clipOn);  //夹住
			arm.setPosition(armUp);  //翻转挂矿石
			upTurn.setPosition(upTurnUp);
			claw.setPosition(clawOpen);
		} else {
			clip.setPosition(clipOpen);  //打开
			arm.setPosition(armMiddle);  //中间等待位置
			upTurn.setPosition(upTurnDown);
		}
	}


	private void clawOperation1(boolean y) {
		if (y) {
			claw.setPosition(clawOpen);   //打开
			turn.setPosition(turnMiddle);  //翻转下去
			rotate.setPosition(rotateOn); //保持水平0.1,0.83
		} else {
			claw.setPosition(clawOn);  //扣住
			turn.setPosition(0.5);  //翻转上去
			rotate.setPosition(rotateOn); //保持水平 0.1,0.83
		}
	}

	private void clipOperation1(boolean y) {
		if (y) {
			clip.setPosition(clipOpen);  //打开
		} else {
			clip.setPosition(clipOn);  //夹住
			claw.setPosition(clawOn);  //扣住
		}
	}

	public void showEncoder() {
		telemetry.addData("lift", leftLift.getCurrentPosition());

		telemetry.addData("touch sensor", touch.isPressed() ? "按了" : "没按");

		telemetry.addData("push", rightPush.getPosition());
		telemetry.addData("clip", clip.getPosition());
		telemetry.addData("trun", turn.getPosition());
		telemetry.addData("claw", claw.getPosition());
		telemetry.addData("rotate", rotate.getPosition());
	}

	//电梯的抬升，为了防止电机高速运转带来的encoder的值的快速变化，当高速抬升到固定的encoder值时，
	// 以匀速低速的形式来实现前抬升的前100转和下降的后100转以低速
	int right_encoder_value = 0;

	private void setLiftPosition(int val) {
		right_encoder_value = val;
	}

	private void LiftPositionUpdate() {
		final int max_position = 2500;//原1000
		final int bufVal = 10;
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
			if (touch.isPressed())//40,210
			{
				leftLift.setPower(0);
				rightLift.setPower(0);
				leftLift.setMode(STOP_AND_RESET_ENCODER);
				leftLift.setMode(RUN_WITHOUT_ENCODER);
				rightLift.setMode(STOP_AND_RESET_ENCODER);
				rightLift.setMode(RUN_WITHOUT_ENCODER);
			} else{
				leftLift.setPower(-1.0);
				rightLift.setPower(-1.0);
			}

		}

	}

	//摆臂复位
	public void arm_clip_Reset() {
		armOperation1(false);

	}

	//上挂舵机复位
	public void rotate_claw_Reset() {
		clawOperation1(false);

	}

	public void clip_Reset() {
		clipOperation1(true);

	}
}

