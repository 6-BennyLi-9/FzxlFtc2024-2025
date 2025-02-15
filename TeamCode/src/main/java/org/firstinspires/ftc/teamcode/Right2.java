package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Utils.armDownR;
import static org.firstinspires.ftc.teamcode.Utils.clipOpen;
import static org.firstinspires.ftc.teamcode.Utils.pushIn;
import static org.firstinspires.ftc.teamcode.Utils.upTurnDownR;
import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(group = "untested", preselectTeleOp = "TELE_RIGHT")
public class Right2 extends LinearOpMode {
	public Utils utils = new Utils();
	public SampleMecanumDrive drive;

	/**
	 * 连贯的抓取样本并倒出的操作
	 */
	public void workingGetSample(){
		utils.claw_rotate_rst(true);  //前面夹子翻转下去、打开
		utils.armOperationL(true);

		utils.claw_rotate_rst(false);//前面夹子翻转下去、打开
		sleep(200);
		utils.clawOperation(false);//夹住
		sleep(200);
		utils.claw_rotate(true);//翻转上去
		//sleep(300);
		utils.setPushPose(pushIn);//前电梯收回
		sleep(200);
		utils.armOperationL(false);//打开夹子，下降去夹
		sleep(250);
		utils.clipOperation(false);//夹住
		sleep(200);
		utils.clawOperation(true);//打开
		sleep(150);
		utils.armOperation(false);//手臂翻转,同时将前夹子放下
		sleep(390);		//这个数值可以控制样本会抛出多远
		utils.clipOperation(true);//打开夹子
		sleep(400);
		utils.armOperationL(true);//翻转回来，打开夹子
	}

	public void workingGetSuspend(Trajectory toGetSample){
		utils.armOperationR(true);//翻转手臂
		utils.clipOperation(true);
		drive.followTrajectory(toGetSample);
		utils.clipOperation(false);    //夹住第一个
		sleep(300);
		utils.setRearLiftPosition(RearLiftLocation.middle);
		utils.armOperationR(false);
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

		Pose2d blueRight            = new Pose2d(- 12, 58, toRadians(- 90));
		Pose2d forward              = new Pose2d(- 10, 33, toRadians(- 90));
		Pose2d toGetFirstSample     = new Pose2d(- 52, 34, toRadians(- 90));
		Pose2d toGetSecondSample    = new Pose2d(- 61, 34, toRadians(- 90));
		Pose2d toGetSuspendSample   = new Pose2d(- 48, 57, toRadians(- 90));
		Pose2d toBlueSample         = new Pose2d(- 40, 30, toRadians(- 90));
		Pose2d toGetThirdBlueSample = new Pose2d(- 43, 55, toRadians(- 90));
		Pose2d toUpThirdSample      = new Pose2d(- 10.5, 33, toRadians(- 90));
		Pose2d toPark               = new Pose2d(- 49, 55, toRadians(- 90));

		drive.setPoseEstimate(blueRight);

		TrajectorySequence left_put = drive.trajectorySequenceBuilder(blueRight)
				.lineToLinearHeading(forward)
				.forward(7)
				.build();
		Trajectory		  toGetFirst = drive.trajectoryBuilder(left_put.end())
				.lineToLinearHeading(toGetFirstSample)
				.build();
		Trajectory		  toGetSecond = drive.trajectoryBuilder(toGetFirst.end())
				.lineToLinearHeading(toGetSecondSample)
				.build();
		Trajectory		  fromSampleToGetSuspend = drive.trajectoryBuilder(toGetSecond.end())
				.lineToLinearHeading(toGetSuspendSample)
				.build();

		waitForStart();
		if (isStopRequested())return;

		utils.setRearLiftPosition(RearLiftLocation.middle);
		utils.armOperationR(false);
		drive.followTrajectorySequence(left_put); //去上挂
		utils.arm.setPosition(armDownR);
		utils.upTurn.setPosition(upTurnDownR);
		utils.clip.setPosition(clipOpen);
		utils.rearLiftToPosition(860);
		sleep(150);
		utils.setRearLiftPosition(RearLiftLocation.down); //回电梯

		sleep(150);
		drive.followTrajectory(toGetFirst);
		workingGetSample();
		drive.followTrajectory(toGetSecond);
		workingGetSample();
		workingGetSuspend(fromSampleToGetSuspend);

		sleep(Long.MAX_VALUE);
	}
}
