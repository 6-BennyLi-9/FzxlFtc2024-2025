package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.utils.IntegralLinearOp;
import org.firstinspires.ftc.teamcode.autonomous.utils.UtilPoses;

@Autonomous(preselectTeleOp = "19419")
public class BlueLeft extends IntegralLinearOp {
	@Override
	public void initialize() {
		drive.setPoseEstimate(UtilPoses.BlueLeftStart);

		registerTrajectory("suspend preload",generateBuilder(UtilPoses.BlueLeftStart)
				.lineToLinearHeading(UtilPoses.BlueLeftSuspend)
				.build());

		final Pose2d afterPushing=registerTrajectory("push samples",generateSequenceBuilder(UtilPoses.BlueLeftSuspend)
				.strafeLeft(24)
				.turn(Math.toRadians(90))
				.strafeRight(24)
				.back(12)
				.strafeLeft(50)
				.strafeRight(50)
				.back(12)
				.strafeLeft(50)
				.strafeRight(50)
				.back(12)
				.strafeLeft(50)
				.strafeRight(50)
				.build());

		registerTrajectory("intake sample1",generateSequenceBuilder(afterPushing)
				.lineToLinearHeading(UtilPoses.BlueLeftSample1)
				.build());
		registerTrajectory("intake sample1",generateSequenceBuilder(UtilPoses.BlueDecant)
				.lineToLinearHeading(UtilPoses.BlueLeftSample2)
				.build());
		registerTrajectory("intake sample1",generateSequenceBuilder(UtilPoses.BlueDecant)
				.lineToLinearHeading(UtilPoses.BlueLeftSample3)
				.build());
		registerTrajectory("go decant",generateBuilder(afterPushing)
				.lineToLinearHeading(UtilPoses.BlueDecant)
				.build());

		registerTrajectory("park",generateBuilder(UtilPoses.BlueDecant)
				.lineToLinearHeading(UtilPoses.BlueLeftPark)
				.build());
	}

	@Override
	public void linear() {
		runTrajectory("suspend preload");
		utils.liftSuspendHighPrepare().armsToSafePosition().openClip().runCached();
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
