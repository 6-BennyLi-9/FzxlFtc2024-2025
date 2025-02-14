package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Utils.pushIn;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(group = "untested", preselectTeleOp = "TELE_RIGHT")
public class Right2 extends LinearOpMode {
	Utils utils = new Utils();

	@Override
	public void runOpMode() throws InterruptedException {
		utils.init(hardwareMap,telemetry);
		utils.liftMotorInit("leftLift","rightLift","touch");
		utils.servoInit("arm","clip","rotate","turn","claw", "upTurn", "leftPush", "rightPush");
		utils.imuInit();
		utils.armOperation(true);
		utils.claw_rotate(false);
		utils.setPushPose(pushIn); //收前电梯
		utils.motorInit();

		SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

		Pose2d blueRight = new Pose2d(-12,58,Math.toRadians(-90));
		Pose2d forward   = new Pose2d(-6,33,Math.toRadians(-90));
		Pose2d toGetFirstSample = new Pose2d(-49,48,Math.toRadians(-90));
		Pose2d toGetSecondSample = new Pose2d(-43.1,53,Math.toRadians(-90));
		Pose2d toUpSecondBlueSample = new Pose2d(-8,30,Math.toRadians(-90));//44
		Pose2d toBlueSample = new Pose2d(-40,30,Math.toRadians(-90)); //-95
		Pose2d toGetThirdBlueSample = new Pose2d(-43,55,Math.toRadians(-90));
		Pose2d toUpThirdSample = new Pose2d(-10.5,33,Math.toRadians(-90));
		Pose2d toPark = new Pose2d(-49,55,Math.toRadians(-90));

		drive.setPoseEstimate(blueRight);

		TrajectorySequence left_put = drive.trajectorySequenceBuilder(blueRight)
				.lineToLinearHeading(forward)
				.forward(7)
				.build();
		TrajectorySequence toGetFirst = drive.trajectorySequenceBuilder(left_put.end())
				.lineToLinearHeading(toGetFirstSample)
				.build();
		TrajectorySequence toGetSecondBlue = drive.trajectorySequenceBuilder(left_put.end())
				.lineToLinearHeading(toGetSecondSample)
				.back(2.0)
				.build();
		TrajectorySequence toUpSecondBlue = drive.trajectorySequenceBuilder(toGetSecondBlue.end())
				.lineToLinearHeading(toUpSecondBlueSample)
				.forward(5.0)
				.build();
		TrajectorySequence toPut = drive.trajectorySequenceBuilder(left_put.end())
				.lineToLinearHeading(toBlueSample)
				.forward(23)
				.strafeRight(10)
				.back(37)
				.forward(35)
				.strafeRight(10.8)
				.back(37)
				.strafeLeft(10)
				.back(9.0)
				.build();
		TrajectorySequence toGetThirdBlue = drive.trajectorySequenceBuilder(toUpSecondBlue.end())
				.lineToLinearHeading(toGetThirdBlueSample)
				.back(3.1)
				.build();
		TrajectorySequence toUpThirdBlue = drive.trajectorySequenceBuilder(toGetThirdBlue.end())
				.lineToLinearHeading(toUpThirdSample)
				.forward(6.5)
				.build();
		TrajectorySequence park= drive.trajectorySequenceBuilder(toUpThirdBlue.end())
				.lineToLinearHeading(toPark)
				.build();

		waitForStart();
		if (isStopRequested())return;

		utils.setRearLiftPosition(RearLiftLocation.middle);
		utils.armOperationR(false);
		drive.followTrajectorySequence(left_put); //去上挂
		utils.clipOperation(true);
		utils.setRearLiftPosition(RearLiftLocation.down); //回电梯
		sleep(50);
		utils.armOperationR(true);       //翻转手臂

		drive.followTrajectorySequence(toGetFirst);
		utils.claw_rotate_rst(true);  //前面夹子翻转下去、打开
		utils.armOperationL(true);

		utils.claw_rotate_rst(false);  //前面夹子翻转下去、打开
		sleep(200); //500
		utils.clawOperation(false);      //夹住
		sleep(200);
		utils.claw_rotate(true);   //翻转上去
		//sleep(300);
		utils.setPushPose(pushIn);  //前电梯收回
		sleep(200);
		utils.armOperationL(false);     //打开夹子，下降去夹
		sleep(250);
		utils.clipOperation(false);   //夹住
		sleep(200);
		utils.clawOperation(true);   //打开
		sleep(150);


	}
}
