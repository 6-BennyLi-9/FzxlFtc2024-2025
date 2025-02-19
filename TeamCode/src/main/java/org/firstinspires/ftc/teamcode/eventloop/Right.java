package org.firstinspires.ftc.teamcode.eventloop;

import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.LinearEventMode;
import org.firstinspires.ftc.teamcode.RearLiftLocation;

@Autonomous(group = Character.MIN_VALUE + "drive", preselectTeleOp = "挂样本")
public class Right extends LinearEventMode {
	public static final int PUSH_LENGTH      = 32;
	public static       int SUSPEND_TIMES    = 4;
	public static       int DELTA_PLACE_INCH = -3;

	@Override
	public void initialize() {
		Pose2d prePushFirstSample    = new Pose2d(- 45, 9, toRadians(- 90));
		Pose2d afterPushFirstSample  = new Pose2d(- 50, 9 + PUSH_LENGTH, toRadians(- 90));
		Pose2d prePushSecondSample   = new Pose2d(- 50, 9, toRadians(- 90));
		Pose2d toGetSuspendSample    = new Pose2d(- 43, 55, toRadians(- 90));
		Pose2d toSuspendGottenSample = new Pose2d(- 4, 22, toRadians(- 80));

		MAIN_BUILDER
			.strafeRight(5)
			.addDisplacementMarker(() -> service.execute(()->{
				utils.armOperationR(true);//翻转手臂
				utils.clipOperation(true);
			}))
			.lineToSplineHeading(prePushFirstSample)
			.strafeRight(5)
			.lineToSplineHeading(afterPushFirstSample)
			.lineToSplineHeading(prePushSecondSample)
			.strafeRight(10)
			.lineToSplineHeading(toGetSuspendSample.plus(new Pose2d(- 2)))
			.addDisplacementMarker(()->{
				sleep(150);

				service.execute(()->{
					utils.clipOperation(false);    //夹住第一个
					sleep(300);
					utils.setRearLiftPosition(RearLiftLocation.middle);
					utils.armOperationR(false);
				});
			});

		for (int i = 0 ; i < SUSPEND_TIMES ; i++) {
			MAIN_BUILDER
				.splineToConstantHeading(toSuspendGottenSample.plus(new Pose2d(DELTA_PLACE_INCH * i)).vec(),Math.toRadians(0))
				.addDisplacementMarker(()-> service.execute(()->{
					utils.setRearLiftPosition(RearLiftLocation.down); //回电梯
					sleep(150);
					utils.clipOperation(true);
					utils.armOperationR(true);       //翻转手臂
				}))
				.lineToLinearHeading(toGetSuspendSample)
				.addDisplacementMarker(()->{
					sleep(150);

					service.execute(()->{
						utils.clipOperation(false);    //夹住第一个
						sleep(300);
						utils.setRearLiftPosition(RearLiftLocation.middle);
						utils.armOperationR(false);
					});
				});
		}
	}

	@Override
	public Pose2d getInitialPoseEstimate() {
		return new Pose2d(- 35, 58, toRadians(- 90));
	}
}
