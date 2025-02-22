package org.firstinspires.ftc.teamcode.eventloop;

import static org.firstinspires.ftc.teamcode.RearLiftLocation.down;
import static org.firstinspires.ftc.teamcode.RearLiftLocation.middle;
import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.LinearEventMode;

@Disabled
@Autonomous(group = Character.MIN_VALUE + "drive other", preselectTeleOp = "挂样本")
public class Right extends LinearEventMode {
	public final int  PUSH_LENGTH           = 32;
	public       int  SUSPEND_TIMES         = 4;
	public final int  DELTA_PLACE_INCH      = - 3;
	public final int  SUSPEND_DIFF          = 10;
	public       long SLEEP_WHEN_GET_SAMPLE = 150;
	public       long SLEEP_BUF_VALUE       = 50;

	@Override
	public void initialize() {
		Pose2d prePushFirstSample    = new Pose2d(- 45, 9, toRadians(- 90));
		Pose2d afterPushFirstSample  = new Pose2d(- 50, 9 + PUSH_LENGTH, toRadians(- 90));
		Pose2d prePushSecondSample   = new Pose2d(- 50, 9, toRadians(- 90));
		Pose2d toGetSuspendSample    = new Pose2d(- 43, 55, toRadians(- 90));
		Pose2d toSuspendGottenSample = new Pose2d(- 4, 24 + SUSPEND_DIFF, toRadians(- 80));

		MAIN_BUILDER
			.addTemporalMarker(5,() -> service.execute(()->{
				utils.armOperationR(true);//翻转手臂
				utils.clipOperation(true);
			}))
			.strafeRight(5)
			.lineToSplineHeading(prePushFirstSample)
			.strafeRight(5)
			.lineToSplineHeading(afterPushFirstSample)
			.lineToSplineHeading(prePushSecondSample)
			.strafeRight(9)
			.lineToSplineHeading(toGetSuspendSample.plus(new Pose2d(- 2)))
			.addTemporalMarker(()->{
				sleep(SLEEP_WHEN_GET_SAMPLE);

				service.execute(()->{
					utils.clipOperation(false);    //夹住第一个
					sleep(SLEEP_BUF_VALUE * 6);
					utils.setRearLiftPosition(middle);
					utils.armOperationR(false);
				});

				sleep(SLEEP_BUF_VALUE);
			});

		for (int i = 0 ; i < SUSPEND_TIMES ; i++) {
			MAIN_BUILDER
				.lineTo(toSuspendGottenSample.plus(new Pose2d(DELTA_PLACE_INCH * i)).vec())
				.forward(SUSPEND_DIFF)
				.addTemporalMarker(()-> service.execute(()->{
					utils.setRearLiftPosition(down); //回电梯
					sleep(150);
					utils.clipOperation(true);
					utils.armOperationR(true);       //翻转手臂
				}))
				.lineToLinearHeading(toGetSuspendSample)
				.addTemporalMarker(()->{
					sleep(SLEEP_WHEN_GET_SAMPLE);

					service.execute(()->{
						utils.clipOperation(false);    //夹住第一个
						sleep(SLEEP_BUF_VALUE * 6);
						utils.setRearLiftPosition(middle);
						utils.armOperationR(false);
					});

					sleep(SLEEP_BUF_VALUE);
				});
		}
	}

	@Override
	public Pose2d getInitialPoseEstimate() {
		return new Pose2d(- 35, 58, toRadians(- 90));
	}

	@Disabled
	@Autonomous(name = "Right（更低效率）", group = Character.MIN_VALUE + "drive other", preselectTeleOp = "挂样本")
	public static final class LowerGetSamples extends Right {
		public LowerGetSamples() {
			SUSPEND_TIMES = 3;
			SLEEP_WHEN_GET_SAMPLE = 400;
			SLEEP_BUF_VALUE = 100;
		}
	}
}
