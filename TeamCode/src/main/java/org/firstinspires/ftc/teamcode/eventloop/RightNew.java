package org.firstinspires.ftc.teamcode.eventloop;

import static org.firstinspires.ftc.teamcode.RearLiftLocation.down;
import static org.firstinspires.ftc.teamcode.RearLiftLocation.middle;
import static org.firstinspires.ftc.teamcode.Utils.pushIn;
import static org.firstinspires.ftc.teamcode.Utils.pushOut;
import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.LinearEventMode;

@Disabled
@Deprecated
@Autonomous(group = Character.MAX_VALUE + "beta", preselectTeleOp = "挂样本")
public class RightNew extends LinearEventMode {
	public static final int GET_SAMPLES_TIMES = 2;
	public static final int DELTA_GET_INCH    = -11;
	public static final int SUSPEND_TIMES     = 4;
	public static final int DELTA_PLACE_INCH = -3;
	public static final int SUSPEND_DIFF     = 9;

	@Override
	public void initialize() {
		Pose2d originGetBlueSample   = new Pose2d(- 30, 43, toRadians(- 135));
		Pose2d originPutBlueSample   = new Pose2d(- 30, 45, toRadians(100));
		Pose2d toGetSuspendSample    = new Pose2d(- 43, 55, toRadians(- 90));
		Pose2d toSuspendGottenSample = new Pose2d(- 4, 21 + SUSPEND_DIFF, toRadians(- 80));

		for (int i = 0 ; i < GET_SAMPLES_TIMES ; i++) {
			MAIN_BUILDER
					.lineToLinearHeading(originGetBlueSample.plus(new Pose2d(DELTA_GET_INCH * i)))
					.addDisplacementMarker(()->{
						utils.setPushPose(pushOut);
						utils.claw_rotate_rst(false);    //翻转下去，打开
						utils.rotate.setPosition(0.8);
						sleep(500);
						utils.clawOperation(false);      //夹住
						sleep(200);
					})
					.lineToSplineHeading(originPutBlueSample.plus(new Pose2d(DELTA_GET_INCH * i)))
					.addDisplacementMarker(()-> {
						sleep(500);
						utils.clawOperation(true);
					});
		}

		MAIN_BUILDER
				.lineToLinearHeading(toGetSuspendSample)
				.addDisplacementMarker(()-> {
					utils.claw_rotate(true);
					utils.setPushPose(pushIn);

					sleep(150);

					service.execute(()->{
						utils.clipOperation(false);    //夹住第一个
						sleep(300);
						utils.setRearLiftPosition(middle);
						utils.armOperationR(false);
					});
				});

		for (int i = 0 ; i < SUSPEND_TIMES ; i++) {
			MAIN_BUILDER
					.lineTo(toSuspendGottenSample.plus(new Pose2d(DELTA_PLACE_INCH * i)).vec())
					.forward(SUSPEND_DIFF)
					.addDisplacementMarker(()-> service.execute(()->{
						utils.setRearLiftPosition(down); //回电梯
						sleep(150);
						utils.clipOperation(true);
						utils.armOperationR(true);       //翻转手臂
					}))
					.lineToLinearHeading(toGetSuspendSample)
					.addDisplacementMarker(()->{
						sleep(200);

						service.execute(()->{
							utils.clipOperation(false);    //夹住
							sleep(300);
							utils.setRearLiftPosition(middle);
							utils.armOperationR(false);
						});
					});
		}
	}

	@Override
	public Pose2d getInitialPoseEstimate() {
		return new Pose2d(- 35, 58, toRadians(- 135));
	}
}
