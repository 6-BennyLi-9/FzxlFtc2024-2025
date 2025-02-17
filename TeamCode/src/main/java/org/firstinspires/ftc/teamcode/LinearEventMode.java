package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Utils.pushIn;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Const;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequenceBuilder;

public abstract class LinearEventMode extends LinearOpMode {
	public TrajectorySequence 		 MAIN_SEQUENCE;
	public TrajectorySequenceBuilder MAIN_BUILDER;
	public Utils					 utils;
	public SampleMecanumDrive		 drive;

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
		utils.imuInit();
		utils.armOperation(true);
		utils.claw_rotate(false);
		utils.setPushPose(pushIn); //收前电梯
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
