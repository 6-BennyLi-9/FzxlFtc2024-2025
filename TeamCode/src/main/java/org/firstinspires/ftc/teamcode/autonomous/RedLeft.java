package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.utils.IntegralLinearOp;

@Autonomous(preselectTeleOp = "19419",group = "0_main")
public class RedLeft extends IntegralLinearOp {
	@Override
	public void initialize() {
		drive.setPoseEstimate(UtilPoses.RedLeftStart);

		registerTrajectory("suspend preload",generateBuilder(UtilPoses.RedLeftStart)
				.lineToLinearHeading(UtilPoses.RedLeftSuspend)
				.build());

		final Pose2d afterPushing=registerTrajectory("push samples",generateSequenceBuilder(UtilPoses.RedLeftSuspend)
				.strafeRight(24)
				.back(12)
				.strafeRight(24)
				.build());

		registerTrajectory("intake sample1",generateSequenceBuilder(afterPushing)
				.lineToLinearHeading(UtilPoses.RedLeftSample1)
				.build());
		registerTrajectory("intake sample1",generateSequenceBuilder(UtilPoses.RedDecant)
				.lineToLinearHeading(UtilPoses.RedLeftSample2)
				.build());
		registerTrajectory("intake sample1",generateSequenceBuilder(UtilPoses.RedDecant)
				.lineToLinearHeading(UtilPoses.RedLeftSample3)
				.build());
		registerTrajectory("go decant",generateBuilder(afterPushing)
				.lineToLinearHeading(UtilPoses.RedDecant)
				.build());

		registerTrajectory("park",generateBuilder(UtilPoses.RedDecant)
				.lineToLinearHeading(UtilPoses.RedLeftPark)
				.build());
	}

	@Override
	public void linear() {
		utils.liftSuspendHighPrepare().armsToSafePosition().openClip().runCached();
		runTrajectory("suspend preload");
		utils.liftSuspendHigh().runCached();
		utils.openClip().liftDown().runCached();
		runTrajectory("push samples");
		runTrajectory("intake sample1");
		utils.integralIntakes().runCached();
		sleep(1000);
		utils.integralIntakesEnding().runCached();
		runTrajectory("go decant");
		utils.integralLiftUpPrepare().liftDecantHigh().runCached();
		utils.decant();
		utils.integralLiftDownPrepare().liftDown().runCached();

		runTrajectory("intake sample2");
		utils.integralIntakes().runCached();
		sleep(1000);
		utils.integralIntakesEnding().runCached();
		runTrajectory("go decant");
		utils.integralLiftUpPrepare().liftDecantHigh().runCached();
		utils.decant();
		utils.integralLiftDownPrepare().liftDown().runCached();

		runTrajectory("intake sample3");
		utils.integralIntakes().runCached();
		sleep(1000);
		utils.integralIntakesEnding().runCached();
		runTrajectory("go decant");
		utils.integralLiftUpPrepare().liftDecantHigh().runCached();
		utils.decant();
		utils.integralLiftDownPrepare().liftDown().runCached();

		runTrajectory("park");
	}
}