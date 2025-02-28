package org.firstinspires.ftc.teamcode.eventloop;

import static org.firstinspires.ftc.teamcode.RearLiftLocation.down;
import static org.firstinspires.ftc.teamcode.RearLiftLocation.middle;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.TeamLinearMode;
import org.firstinspires.ftc.teamcode.roadrunner.sequence.TrajectorySequence;

@Autonomous(group = Character.MIN_VALUE + "drive",preselectTeleOp = "挂样本")
public class RightAuto extends TeamLinearMode {
	@Override
	public void onRunning() {
		Pose2d blueRight            = new Pose2d(- 12, 58, Math.toRadians(- 90));
		Pose2d forward              = new Pose2d(- 6, 33, Math.toRadians(- 90));
		Pose2d toGetSecondSample    = new Pose2d(- 43.1, 51.5, Math.toRadians(- 90));
		Pose2d toUpSecondBlueSample = new Pose2d(- 7.5, 30, Math.toRadians(- 90));//44
		Pose2d toBlueSample         = new Pose2d(- 35, 37, Math.toRadians(- 90)); //-95
		Pose2d toGetThirdBlueSample = new Pose2d(- 43, 55, Math.toRadians(- 90));
		Pose2d toUpThirdSample      = new Pose2d(- 1, 33, Math.toRadians(- 90));
		Pose2d toPark               = new Pose2d(- 49, 55, Math.toRadians(- 90));

		drive.setPoseEstimate(blueRight);

		TrajectorySequence left_put = drive.trajectorySequenceBuilder(blueRight)
				.lineToLinearHeading(forward)
				.forward(7)
				.build();
		TrajectorySequence toGetSecondBlue = drive.trajectorySequenceBuilder(left_put.end())
				.lineToLinearHeading(toGetSecondSample)
				.back(1.8)
				.build();

		TrajectorySequence toUpSecondBlue = drive.trajectorySequenceBuilder(toGetSecondBlue.end())
				.lineToLinearHeading(toUpSecondBlueSample)
				.forward(6.0)
				.build();

		TrajectorySequence toPush = drive.trajectorySequenceBuilder(left_put.end())
				.back(5)
				.lineToLinearHeading(toBlueSample)
				.forward(30)
				.strafeRight(9)
				.back(37)
				.forward(37)
				.strafeRight(7)
				.back(37)
				.strafeLeft(8)
				.back(8.5)
				.build();
		TrajectorySequence toGetThirdBlue = drive.trajectorySequenceBuilder(toUpSecondBlue.end())
				.lineToLinearHeading(toGetThirdBlueSample)
				.back(2.8)
				.build();

		TrajectorySequence ToUpThirdBlue = drive.trajectorySequenceBuilder(toGetThirdBlue.end())
				.lineToLinearHeading(toUpThirdSample)
				.forward(12)
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


		utils.setRearLiftPosition(middle);
		utils.armOperationR(false);
		drive.followTrajectorySequence(left_put); //去上挂
		utils.clipOperation(true);
		utils.setRearLiftPosition(down); //回电梯
		sleep(50);
		utils.armOperationR(true);       //翻转手臂
		//夹第一个
		drive.followTrajectorySequence(toGetSecondBlue); //去推第一样本
		sleep(550);
		utils.clipOperation(false);    //夹住第一个
		sleep(400);
		utils.setRearLiftPosition(middle);
		utils.armOperationR(false);
		drive.followTrajectorySequence(toUpSecondBlue);  //上挂第二个样本
		sleep(100);
		utils.clipOperation(true);
		utils.setRearLiftPosition(down); //回电梯
		sleep(50);
		utils.armOperationR(true);
		//去夹第二个
		drive.followTrajectorySequence(toPush);
		sleep(550);
		utils.clipOperation(false);
		sleep(400);
		utils.setRearLiftPosition(middle);
		utils.armOperationR(false);
		drive.followTrajectorySequence(ToUpThirdBlue);  //上挂第二个样本
		sleep(100);//150
		utils.clipOperation(true);
		utils.setRearLiftPosition(down); //回电梯
		drive.followTrajectorySequence(Park);//去拿第三个
		utils.armOperationR(true);
		utils.claw_rotate(true);

		sleep(Long.MAX_VALUE);
	}
}
