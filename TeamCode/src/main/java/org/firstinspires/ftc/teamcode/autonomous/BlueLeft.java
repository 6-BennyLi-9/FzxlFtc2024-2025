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
				.lineToLinearHeading(UtilPoses.BlueDecant)
				.build());

		Pose2d afterPushing=registerTrajectory("push samples",generateSequenceBuilder(UtilPoses.BlueDecant)
				.strafeLeft(24)
				.forward(12)
				.strafeRight(36)
				.build());
	}

	@Override
	public void linear() {
		runTrajectory("suspend preload");
		utils.armsToSafePosition().openClip().runCached();
		sleep(1000);
		runTrajectory("push samples");
	}
}
