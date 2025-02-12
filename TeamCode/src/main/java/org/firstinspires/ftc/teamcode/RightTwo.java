package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;


//12.5*17 (in)
//6*9
//arm和claw的值需要确定
@Config
@Disabled
@Autonomous(name = "Right-上挂", group = "drive")
public class RightTwo extends LinearOpMode {
	Utils utils = new Utils();

	@Override
	public void runOpMode() throws InterruptedException {

		utils.init(hardwareMap, telemetry);
		utils.liftMotorInit("frontLift", "lift", "touch");
		utils.servoInit("arm", "clip", "rotate", "turn", "claw", "upTurn", "leftPush", "rightPush");
		utils.imuInit();
		utils.armOperation1(true);
		utils.claw_rotate(false);
		utils.motorInit();


		SampleMecanumDrive drive        = new SampleMecanumDrive(hardwareMap);
		Pose2d             blueRight    = new Pose2d(- 12, 58, Math.toRadians(90));
		Pose2d             forward      = new Pose2d(- 8, 33, Math.toRadians(90));
		Pose2d             toBlueSample = new Pose2d(- 40, 30, Math.toRadians(90));

		Pose2d toUpSecondBlueSample = new Pose2d(- 10, 30, Math.toRadians(90));//44

		Pose2d toGetThirdBlueSample = new Pose2d(- 43, 54, Math.toRadians(90));

		Pose2d toUpThirdSample  = new Pose2d(- 10, 33, Math.toRadians(90));
		Pose2d toGetFirstSample = new Pose2d(- 56, 34, Math.toRadians(90));
		Pose2d toTurn           = new Pose2d(- 12, 37.5, Math.toRadians(90));
		Pose2d toPark           = new Pose2d(- 68.5, 52.5, Math.toRadians(90));


		drive.setPoseEstimate(blueRight);

		TrajectorySequence left_put = drive.trajectorySequenceBuilder(blueRight)
				.lineToLinearHeading(forward)
				.back(7)
				.build();
		TrajectorySequence toPut = drive.trajectorySequenceBuilder(left_put.end())
				.lineToLinearHeading(toBlueSample)
				.back(25)
				.strafeLeft(10)
				.forward(38)
				.back(37)
				.strafeLeft(10)
				.forward(38)   //推第二个
				//.back(37)
				// .strafeLeft(7)
				// .forward(37)
				//.strafeRight(15)
				.strafeRight(10)
				.forward(10)
				.build();
		TrajectorySequence toUpSecondBlue = drive.trajectorySequenceBuilder(toPut.end())
				.lineToLinearHeading(toUpSecondBlueSample)
				.back(8)
				.build();

		TrajectorySequence toGetThirdBlue = drive.trajectorySequenceBuilder(toUpSecondBlue.end())
				.lineToLinearHeading(toGetThirdBlueSample)
				.forward(6)
				.build();

		TrajectorySequence ToUpThirdBlue = drive.trajectorySequenceBuilder(toGetThirdBlue.end())
				.lineToLinearHeading(toUpThirdSample)
				.back(9)
				.build();
		TrajectorySequence Turn = drive.trajectorySequenceBuilder(ToUpThirdBlue.end())
				.lineToLinearHeading(toTurn)
				.build();
		Trajectory UpFirst = drive.trajectoryBuilder(Turn.end())
				.lineToLinearHeading(toGetFirstSample)
				.build();
		Trajectory Park = drive.trajectoryBuilder(UpFirst.end())
				.lineToLinearHeading(toPark)
				.build();

		telemetry.addData(">>>", "Robot Ready");
		telemetry.update();

		telemetry.addLine("Waiting for start");
		telemetry.update();

		waitForStart();

		if (isStopRequested()) return;

		utils.rearLiftPosition(RearLiftLocation.middle);
		utils.armOperationR(false);
		drive.followTrajectorySequence(left_put); //去上挂
		utils.rearLiftPosition(RearLiftLocation.down); //回电梯
		utils.armOperationR(true);       //翻转手臂
		//夹第一个
		utils.angleCalibration(0, toPut.start(), drive);  //ium校准方向
		drive.followTrajectorySequence(toPut);    //去推第一样本
		sleep(500);
		utils.clipOperation(false);    //夹住第一个
		sleep(100);
		utils.rearLiftPosition(RearLiftLocation.middle);
		utils.armOperationR(false);
		utils.angleCalibration(0, toUpSecondBlue.start(), drive);  //ium校准方向
		drive.followTrajectorySequence(toUpSecondBlue);  //上挂第二个样本
		sleep(150);
		utils.rearLiftPosition(RearLiftLocation.down); //回电梯
		utils.armOperationR(true);
		//去夹第二个
		utils.angleCalibration(0, toGetThirdBlue.start(), drive);
		drive.followTrajectorySequence(toGetThirdBlue);    //
		sleep(500);
		utils.clipOperation(false);
		sleep(100);
		utils.rearLiftPosition(RearLiftLocation.middle);
		utils.armOperationR(false);
		utils.angleCalibration(0, ToUpThirdBlue.start(), drive); //ium校准方向
		drive.followTrajectorySequence(ToUpThirdBlue);  //上挂第二个样本
		sleep(150);
		utils.rearLiftPosition(RearLiftLocation.down); //回电梯
		utils.armOperationR(true);
		//去拿第三个
		utils.angleCalibration(0, toGetThirdBlue.start(), drive); //ium校准方向
		drive.followTrajectorySequence(toGetThirdBlue);
	}
}
