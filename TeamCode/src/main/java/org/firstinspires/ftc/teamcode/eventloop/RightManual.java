package org.firstinspires.ftc.teamcode.eventloop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SuperStructuresR;
import org.firstinspires.ftc.teamcode.WyMoveMethod;

@TeleOp(name = "挂样本", group = "team")
public class RightManual extends OpMode {
	private final WyMoveMethod     carChassis = new WyMoveMethod();
	private final SuperStructuresR structures = new SuperStructuresR();
	private       long             st, et;

	@Override
	public void init() {
		carChassis.init(hardwareMap, telemetry, gamepad1, gamepad2);
		//carChassis.imuInit("imu");
		carChassis.motorInit("leftFront", "leftRear", "rightFront", "rightRear");
		structures.init(hardwareMap, telemetry, gamepad2);
		structures.servoInit("arm", "turn", "clip", "rotate", "claw", "upTurn", "leftPush", "rightPush");

		structures.motorInit("leftLift", "rightLift", "touch");

		//获取外部三个编码轮的的函数，按次序获取，左、右、中编码器的值，将其初始化
		carChassis.extraEncoderInit(carChassis.get_left_encoder(), carChassis.get_right_encoder(), carChassis.get_mid_encoder());

//		structures.setPushPose(0.15); 不是滑轨自动伸出的原因
		telemetry.addData("State", "初始化完毕");
		telemetry.update();
	}

	@Override
	public void loop() {
		telemetry.addData("FPS", 1000.0 / (et - st));
		st = System.currentTimeMillis();
		telemetry.addLine("dir");
		telemetry.addData("time",getRuntime());

		carChassis.basicMoveThroughGamePad();
		structures.optionThroughGamePad();

		et = System.currentTimeMillis();
		telemetry.update();
	}
}
