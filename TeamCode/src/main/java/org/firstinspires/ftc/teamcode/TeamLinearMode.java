package org.firstinspires.ftc.teamcode;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class TeamLinearMode extends LinearOpMode {
	public final ExecutorService    service = new ThreadPoolExecutor(
			5,
			8,
			1L,
			SECONDS,
			new ArrayBlockingQueue <>(16),
			Executors.defaultThreadFactory(),
			new ThreadPoolExecutor.CallerRunsPolicy()
	);
	public       Utils              utils;
	public       SampleMecanumDrive drive;

	@Override
	public void runOpMode() throws InterruptedException {
		telemetry.addData("Status","正在初始化");
		telemetry.addLine("不要在这个阶段启动程序！");
		telemetry.update();

		drive=new SampleMecanumDrive(hardwareMap);
		utils=new Utils();

		utils.init(hardwareMap, telemetry);
		utils.liftMotorInit("leftLift", "rightLift", "touch");
		utils.servoInit("arm", "clip", "rotate", "turn", "claw", "upTurn", "leftPush", "rightPush");
		utils.armOperation(true);
		utils.claw_rotate(false);
		utils.setPushPose(HardwareParams.pushIn); //收前电梯
		utils.motorInit();

		onRunning();
	}

	public abstract void onRunning();
}
