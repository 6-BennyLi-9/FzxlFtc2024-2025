package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.utils.IntegralLinearOpMode;
import org.firstinspires.ftc.teamcode.autonomous.utils.UtilPoses;

@Autonomous(preselectTeleOp = "19419")
public class RedRight extends IntegralLinearOpMode {
	@Override
	public void initialize() {
		drive.setPoseEstimate(UtilPoses.RedRightStart);

		registerTrajectory("suspend preload",generateBuilder(UtilPoses.RedRightStart)
				.lineToLinearHeading(UtilPoses.RedRightSuspend)
				.build());

		final Pose2d afterPushing=registerTrajectory("push samples",generateSequenceBuilder(UtilPoses.RedRightSuspend)
				.strafeRight(24)
				.back(12)
				.strafeRight(24)
				.build());

		registerTrajectory("get sample1",generateSequenceBuilder(afterPushing)
				.lineToLinearHeading(UtilPoses.RedGetSample)
				.build());
		registerTrajectory("suspend1",generateBuilder(UtilPoses.RedGetSample)
				.lineToLinearHeading(UtilPoses.RedRightSuspend.plus(new Pose2d(-5,0)))
				.build());

		registerTrajectory("get sample u",generateSequenceBuilder(UtilPoses.RedRightSuspend)
				.lineToLinearHeading(UtilPoses.RedGetSample)
				.build());

		registerTrajectory("suspend2",generateBuilder(UtilPoses.RedGetSample)
				.lineToLinearHeading(UtilPoses.RedRightSuspend.plus(new Pose2d(5,0)))
				.build());
		registerTrajectory("suspend3",generateBuilder(UtilPoses.RedGetSample)
				.lineToLinearHeading(UtilPoses.RedRightSuspend.plus(new Pose2d(10,0)))
				.build());


		registerTrajectory("park",generateBuilder(UtilPoses.RedRightSuspend)
				.lineToLinearHeading(UtilPoses.RedGetSample)
				.lineToLinearHeading(UtilPoses.RedRightPark)
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