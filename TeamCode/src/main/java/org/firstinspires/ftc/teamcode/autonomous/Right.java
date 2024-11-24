package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.utils.IntegralLinearOp;

@Autonomous(preselectTeleOp = "19419",group = "0_main")
public class Right extends IntegralLinearOp {
	@Override
	public void initialize() {
		drive.setPoseEstimate(UtilPoses.BlueRightStart);

		registerTrajectory("suspend preload",generateBuilder(UtilPoses.BlueRightStart)
				.lineToLinearHeading(UtilPoses.BlueRightSuspend)
				.build());

		final Pose2d afterPushing=registerTrajectory("push samples",generateSequenceBuilder(UtilPoses.BlueRightSuspend)
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

		registerTrajectory("get sample1",generateSequenceBuilder(afterPushing)
				.lineToLinearHeading(UtilPoses.BlueGetSample)
				.build());
		registerTrajectory("suspend1",generateBuilder(UtilPoses.BlueGetSample)
				.lineToLinearHeading(UtilPoses.BlueRightSuspend.plus(new Pose2d(-5,0)))
				.build());

		registerTrajectory("get sample u",generateSequenceBuilder(UtilPoses.BlueRightSuspend)
				.lineToLinearHeading(UtilPoses.BlueGetSample)
				.build());

		registerTrajectory("suspend2",generateBuilder(UtilPoses.BlueGetSample)
				.lineToLinearHeading(UtilPoses.BlueRightSuspend.plus(new Pose2d(5,0)))
				.build());
		registerTrajectory("suspend3",generateBuilder(UtilPoses.BlueGetSample)
				.lineToLinearHeading(UtilPoses.BlueRightSuspend.plus(new Pose2d(10,0)))
				.build());


		registerTrajectory("park",generateBuilder(UtilPoses.BlueRightSuspend)
				.lineToLinearHeading(UtilPoses.BlueGetSample)
				.lineToLinearHeading(UtilPoses.BlueRightPark)
				.build());
	}

	@Override
	public void linear() {
		runTrajectory("suspend preload");
		utils.liftSuspendHighPrepare().armsToSafePosition().openClip().runCached();
		utils.liftSuspendHigh().runCached();
		utils.openClip().liftDown().runCached();
		runTrajectory("push samples");

		runTrajectory("get sample1");
		utils.closeClip().runCached();
		runTrajectory("suspend1");
		utils.integralLiftUpPrepare().liftSuspendHighPrepare().runCached();
		utils.liftSuspendHigh().runCached();
		utils.openClip().integralLiftDownPrepare().liftDown().runCached();

		runTrajectory("get sample u");
		utils.closeClip().runCached();
		runTrajectory("suspend2");
		utils.integralLiftUpPrepare().liftSuspendHighPrepare().runCached();
		utils.liftSuspendHigh().runCached();
		utils.openClip().integralLiftDownPrepare().liftDown().runCached();

		runTrajectory("get sample u");
		utils.closeClip().runCached();
		runTrajectory("suspend3");
		utils.integralLiftUpPrepare().liftSuspendHighPrepare().runCached();
		utils.liftSuspendHigh().runCached();
		utils.openClip().integralLiftDownPrepare().liftDown().runCached();

		runTrajectory("park");
	}
}