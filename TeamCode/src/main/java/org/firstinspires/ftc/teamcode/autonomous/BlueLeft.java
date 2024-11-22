package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.utils.IntegralLinearOpMode;
import org.firstinspires.ftc.teamcode.autonomous.utils.UtilPoses;

@Autonomous(preselectTeleOp = "19419")
public class BlueLeft extends IntegralLinearOpMode {

	@Override
	public void initialize() {
		drive.setPoseEstimate(UtilPoses.BlueLeftStart);

		registerTrajectory("suspend preload",generateBuilder(UtilPoses.BlueLeftStart)
				.lineToLinearHeading(UtilPoses.BlueLeftSuspend)
				.build());

		Pose2d afterPushing=registerTrajectory("push samples",generateSequenceBuilder(UtilPoses.BlueLeftSuspend)
				.strafeRight(24)
				.back(12)
				.strafeRight(24)
				.build());

		registerTrajectory("intake sample1",generateSequenceBuilder(afterPushing)
				.lineToLinearHeading(UtilPoses.BlueLeftSample1)
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
	}
}
