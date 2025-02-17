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

		carChassis.motorInit("leftFront", "leftRear", "rightFront", "rightRear");
		structures.init(hardwareMap, telemetry, gamepad1, gamepad2);
		structures.servoInit("arm", "turn", "clip", "rotate", "claw", "upTurn", "leftPush", "rightPush");

		structures.motorInit("leftLift", "rightLift", "touch");

		//获取外部三个编码轮的的函数，按次序获取，左、右、中编码器的值，将其初始化
		carChassis.extraEncoderInit(carChassis.get_left_encoder(), carChassis.get_right_encoder(), carChassis.get_mid_encoder());

//		structures.setPushPose(0.15); 不是滑轨自动伸出的原因
		telemetry.addData("state", "初始化完毕");
		telemetry.update();
	}

	@Override
	public void loop() {
		telemetry.addData("FPS", 1000.0 / (et - st));
		st = System.currentTimeMillis();
		telemetry.addLine("dir");
		telemetry.addData("time",getRuntime());

		carChassis.basicMoveThroughGamePad();
		structures.showEncoder();
		structures.optionThroughGamePad();

		et = System.currentTimeMillis();
		telemetry.update();
	}
}
