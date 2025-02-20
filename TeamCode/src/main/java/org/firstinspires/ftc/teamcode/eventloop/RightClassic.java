package org.firstinspires.ftc.teamcode.eventloop;

import static org.firstinspires.ftc.teamcode.Utils.pushIn;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RearLiftLocation;
import org.firstinspires.ftc.teamcode.Utils;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.sequence.TrajectorySequence;

@Autonomous(group = "classic",preselectTeleOp = "挂样本")
public class RightClassic extends LinearOpMode {
	public Utils              utils = new Utils();
	public SampleMecanumDrive drive;

	@Override
	public void runOpMode() throws InterruptedException {
		utils.init(hardwareMap,telemetry);
		utils.liftMotorInit("leftLift","rightLift","touch");
		utils.servoInit("arm","clip","rotate","turn","claw", "upTurn", "leftPush", "rightPush");
		utils.armOperation(true);
		utils.claw_rotate(false);
		utils.setPushPose(pushIn); //收前电梯
		utils.motorInit();

		drive = new SampleMecanumDrive(hardwareMap);

		Pose2d blueRight            = new Pose2d(- 12, 58, Math.toRadians(- 90));
		Pose2d forward              = new Pose2d(- 6, 33, Math.toRadians(- 90));
		Pose2d toGetSecondSample    = new Pose2d(- 43.1, 53, Math.toRadians(- 90));
		Pose2d toUpSecondBlueSample = new Pose2d(- 8, 30, Math.toRadians(- 90));//44
		Pose2d toBlueSample         = new Pose2d(- 40, 30, Math.toRadians(- 90)); //-95
		Pose2d toGetThirdBlueSample = new Pose2d(- 43, 55, Math.toRadians(- 90));
		Pose2d toUpThirdSample      = new Pose2d(- 10.5, 33, Math.toRadians(- 90));
		Pose2d toPark               = new Pose2d(- 49, 55, Math.toRadians(- 90));

		drive.setPoseEstimate(blueRight);

		TrajectorySequence left_put = drive.trajectorySequenceBuilder(blueRight)
				.lineToLinearHeading(forward)
				.forward(7)
				.build();
		TrajectorySequence toGetSecondBlue = drive.trajectorySequenceBuilder(left_put.end())
				.lineToLinearHeading(toGetSecondSample)
				.back(2.0)
				.build();

		TrajectorySequence toUpSecondBlue = drive.trajectorySequenceBuilder(toGetSecondBlue.end())
				.lineToLinearHeading(toUpSecondBlueSample)
				.forward(6.0)
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
				.back(8.5)
				.build();
		TrajectorySequence toGetThirdBlue = drive.trajectorySequenceBuilder(toUpSecondBlue.end())
				.lineToLinearHeading(toGetThirdBlueSample)
				.back(3)
				.build();

		TrajectorySequence ToUpThirdBlue = drive.trajectorySequenceBuilder(toGetThirdBlue.end())
				.lineToLinearHeading(toUpThirdSample)
				.forward(8.0)
				.build();
		TrajectorySequence Park= drive.trajectorySequenceBuilder(ToUpThirdBlue.end())
				.lineToLinearHeading(toPark)
				.build();


		telemetry.addData(">>>", "Robot Ready");
		telemetry.update();

		telemetry.addLine("Waiting for start");
		telemetry.update();

		waitForStart();

		if (isStopRequested()) return;


		utils.setRearLiftPosition(RearLiftLocation.middle);
		utils.armOperationR(false);
		drive.followTrajectorySequence(left_put); //去上挂
		utils.clipOperation(true);
		utils.setRearLiftPosition(RearLiftLocation.down); //回电梯
		sleep(50);
		utils.armOperationR(true);       //翻转手臂
		//夹第一个
		drive.followTrajectorySequence(toGetSecondBlue); //去推第一样本
		sleep(550);
		utils.clipOperation(false);    //夹住第一个
		sleep(400);
		utils.setRearLiftPosition(RearLiftLocation.middle);
		utils.armOperationR(false);
		drive.followTrajectorySequence(toUpSecondBlue);  //上挂第二个样本
		sleep(100);
		utils.clipOperation(true);
		utils.setRearLiftPosition(RearLiftLocation.down); //回电梯
		sleep(50);
		utils.armOperationR(true);
		//去夹第二个
		//
		drive.followTrajectorySequence(toPut);
		sleep(550);
		utils.clipOperation(false);
		sleep(400);
		utils.setRearLiftPosition(RearLiftLocation.middle);
		utils.armOperationR(false);
		drive.followTrajectorySequence(ToUpThirdBlue);  //上挂第二个样本
		sleep(100);//150
		utils.clipOperation(true);
		utils.setRearLiftPosition(RearLiftLocation.down); //回电梯

		//去拿第三个
		drive.followTrajectorySequence(Park);
		utils.armOperationR(true);
		utils.claw_rotate(true);

		sleep(Long.MAX_VALUE);
	}
}
