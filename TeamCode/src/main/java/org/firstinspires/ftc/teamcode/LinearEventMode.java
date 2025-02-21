package org.firstinspires.ftc.teamcode;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Const;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.sequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.roadrunner.sequence.TrajectorySequenceBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class LinearEventMode extends LinearOpMode {
	public TrajectorySequence        MAIN_SEQUENCE;
	public TrajectorySequenceBuilder MAIN_BUILDER;
	public Utils                     utils;
	public SampleMecanumDrive        drive;

	public final ExecutorService service = new ThreadPoolExecutor(
			5,
			8,
			1L,
			SECONDS,
			new ArrayBlockingQueue <>(16),
			Executors.defaultThreadFactory(),
			new ThreadPoolExecutor.CallerRunsPolicy()
	);

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
		utils.setPushPose(HardwareDatabase3.pushIn); //收前电梯
		utils.motorInit();
		drive.setPoseEstimate(getInitialPoseEstimate());
		MAIN_BUILDER = drive.trajectorySequenceBuilder(getInitialPoseEstimate());

		initialize();
		MAIN_SEQUENCE=MAIN_BUILDER.build();

		telemetry.addData("Status","初始化已完成");
		telemetry.update();

		waitForStart();
		if (isStopRequested()) {
			return;
		}
		drive.followTrajectorySequence(MAIN_SEQUENCE);
	}

	/**
	 * 要求：修改 MAIN_BUILDER
	 */
	public abstract void initialize();
	@Const
	public abstract Pose2d getInitialPoseEstimate();
}
