package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "放篮子", group = "aba_aba")

public class Wy_textL extends OpMode {
	private final WyMoveMethod     carChassis = new WyMoveMethod();
	private final SuperStructuresL structures = new SuperStructuresL();
	private       long             st, et;

	@Override
	public void init() {
		carChassis.init(hardwareMap, telemetry, gamepad1, gamepad2);
		//carChassis.imuInit("imu");
		carChassis.motorInit("leftFront", "leftRear", "rightFront", "rightRear");
		structures.init(hardwareMap, telemetry, gamepad1, gamepad2);
		structures.servoInit("arm", "turn", "clip", "rotate", "claw", "upTurn", "leftPush", "rightPush");
		structures.arm_clip_Reset();
		structures.rotate_claw_Reset();
		structures.motorInit("leftLift", "rightLift", "touch");


		//DcMotor encoder = hardwareMap.get(DcMotor.class, "encoder");//已修改
		//encoder.setDirection(DcMotorSimple.Direction.REVERSE);
		//获取外部三个编码轮的的函数，按次序获取，左、右、中编码器的值，将其初始化
		carChassis.extraEncoderInit(carChassis.get_left_encoder(), carChassis.get_right_encoder(), carChassis.get_mid_encoder());

		telemetry.addData("state", "初始化完毕");
	}

	@Override
	public void loop() {
		telemetry.addData("FPS", 1 / ((et - st) / 100.0));
		st = System.currentTimeMillis();
		telemetry.addLine("dir");
		carChassis.basicMoveThroughGamePad();
		//carChassis.showIMU();
		//        carChassis.showEncoders();
		structures.showEncoder();
		structures.optionThroughGamePad();
		//structures.test(); //测试舵机位置程序
		//structures.LiftEncoderTest();//测试电梯的位置
		et = System.currentTimeMillis();
		telemetry.update();
	}

	@Override
	public void stop() {
		telemetry.addData("stop", "stop");
		telemetry.update();
	}
}
