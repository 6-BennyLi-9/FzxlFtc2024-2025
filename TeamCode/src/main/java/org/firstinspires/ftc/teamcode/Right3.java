package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Utils.pushIn;
import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(group = "untested", preselectTeleOp = "TELE_RIGHT")
public class Right3 extends LinearOpMode {
	public static final int PUSH_LENGTH = 32;

	public Utils              utils = new Utils();
	public SampleMecanumDrive drive;

	public void workingGetSuspend(Trajectory toGetSample) {
		workingGetSuspend(drive.trajectorySequenceBuilder(drive.getPoseEstimate()).addTrajectory(toGetSample).build());
	}

	public void workingGetSuspend(TrajectorySequence toGetSample){
		utils.armOperationR(true);//翻转手臂
		utils.clipOperation(true);
		drive.followTrajectorySequence(toGetSample);
		utils.clipOperation(false);    //夹住第一个
		sleep(300);
		utils.setRearLiftPosition(RearLiftLocation.middle);
		utils.armOperationR(false);
	}
	public void workingSuspend(TrajectorySequence toSuspend){
		utils.setRearLiftPosition(RearLiftLocation.middle);
		utils.armOperationR(false);
		drive.followTrajectorySequence(toSuspend); //去上挂
		utils.clipOperation(true);
		utils.setRearLiftPosition(RearLiftLocation.down); //回电梯
		sleep(50);
		utils.armOperationR(true);       //翻转手臂
	}

	@Override
	public void runOpMode() throws InterruptedException {
		utils.init(hardwareMap, telemetry);
		utils.liftMotorInit("leftLift", "rightLift", "touch");
		utils.servoInit("arm", "clip", "rotate", "turn", "claw", "upTurn", "leftPush", "rightPush");
		utils.imuInit();
		utils.armOperation(true);
		utils.claw_rotate(false);
		utils.setPushPose(pushIn); //收前电梯
		utils.motorInit();

		drive = new SampleMecanumDrive(hardwareMap);

		Pose2d blueRight = new Pose2d(- 35, 58, toRadians(- 90));
		Pose2d toGetSuspendSample   = new Pose2d(- 40, 57, toRadians(- 90));
		Pose2d toSuspendGottenSample= new Pose2d(- 10, 33, toRadians(- 90));

		drive.setPoseEstimate(blueRight);

		TrajectorySequence pushSamples = drive.trajectorySequenceBuilder(blueRight)
				.strafeRight(6)
				.forward(50)
				.strafeRight(11)//first
				.back(PUSH_LENGTH)
				.forward(PUSH_LENGTH)
				.strafeRight(10)//second
				.back(PUSH_LENGTH)
//				.forward(PUSH_LENGTH)
//				.strafeRight(7)//third
//				.back(40)
				.build();
		Trajectory toGetSuspend = drive.trajectoryBuilder(pushSamples.end())
				.lineToLinearHeading(toGetSuspendSample)
				.build();
		TrajectorySequence toSuspend = drive.trajectorySequenceBuilder(toGetSuspend.end())
				.lineToLinearHeading(toSuspendGottenSample)
				.forward(10)
				.build();
		Trajectory reloadSuspend = drive.trajectoryBuilder(toSuspend.end())
				.lineToLinearHeading(toSuspend.start())
				.build();

		waitForStart();
		if (isStopRequested())return;

		drive.followTrajectorySequence(pushSamples);
		workingGetSuspend(toGetSuspend);

		workingSuspend(toSuspend);
		workingGetSuspend(reloadSuspend);

		sleep(Long.MAX_VALUE);
	}
}
