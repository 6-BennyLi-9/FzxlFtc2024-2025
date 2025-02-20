package org.firstinspires.ftc.teamcode.eventloop;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.LinearEventMode;
import org.firstinspires.ftc.teamcode.RearLiftLocation;

@Autonomous(group = Character.MIN_VALUE+"beta",preselectTeleOp = "挂样本")
public class RightIntegralClassic extends LinearEventMode {
	@Override
	public void initialize() {
		Pose2d forward              = new Pose2d(- 6, 33, Math.toRadians(- 90));
		Pose2d toGetSecondSample    = new Pose2d(- 43.1, 53, Math.toRadians(- 90));
		Pose2d toUpSecondBlueSample = new Pose2d(- 8, 30, Math.toRadians(- 90));//44
		Pose2d toBlueSample         = new Pose2d(- 40, 30, Math.toRadians(- 90)); //-95
		Pose2d toUpThirdSample      = new Pose2d(- 10.5, 33, Math.toRadians(- 90));
		Pose2d toPark               = new Pose2d(- 49, 55, Math.toRadians(- 90));

		MAIN_BUILDER
				.addTemporalMarker(()->{
					utils.setRearLiftPosition(RearLiftLocation.middle);
					utils.armOperationR(false);
				})
				.lineToLinearHeading(forward)
				.forward(7)
				.addTemporalMarker(()->{
					utils.setRearLiftPosition(RearLiftLocation.down); //回电梯
					sleep(50);
					utils.armOperationR(true);       //翻转手臂
				})
				.lineToLinearHeading(toGetSecondSample)
				.back(2.0)
				.addTemporalMarker(()->{
					sleep(550);
					utils.clipOperation(false);    //夹住第一个
					sleep(400);
					utils.setRearLiftPosition(RearLiftLocation.middle);
					utils.armOperationR(false);
				})
				.lineToLinearHeading(toUpSecondBlueSample)
				.forward(6.0)
				.addTemporalMarker(()->{
					sleep(100);
					utils.clipOperation(true);
					utils.setRearLiftPosition(RearLiftLocation.down); //回电梯
					sleep(50);
					utils.armOperationR(true);
				})
				.lineToLinearHeading(toBlueSample)
				.forward(23)
				.strafeRight(10)
				.back(37)
				.forward(35)
				.strafeRight(10.8)
				.back(37)
				.strafeLeft(10)
				.back(8.5)
				.addTemporalMarker(()->{
					sleep(550);
					utils.clipOperation(false);
					sleep(400);
					utils.setRearLiftPosition(RearLiftLocation.middle);
					utils.armOperationR(false);
				})
				.lineToLinearHeading(toUpThirdSample)
				.forward(8.0)
				.addTemporalMarker(()->{
					sleep(100);//150
					utils.clipOperation(true);
					utils.setRearLiftPosition(RearLiftLocation.down); //回电梯
				})
				.lineToLinearHeading(toPark)
				.addTemporalMarker(()->{
					utils.armOperationR(true);
					utils.claw_rotate(true);
				});
	}

	@Override
	public Pose2d getInitialPoseEstimate() {
		return new Pose2d(- 12, 58, Math.toRadians(- 90));
	}
}
