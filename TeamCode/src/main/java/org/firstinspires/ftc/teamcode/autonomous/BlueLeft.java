package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.utils.IntegralLinearOp;

@Autonomous(preselectTeleOp = "19419",group = "0_main")
public class BlueLeft extends IntegralLinearOp {
	@Override
	public void initialize() {
		drive.setPoseEstimate(UtilPoses.BlueLeftStart);

		registerTrajectory("suspend preload",generateBuilder(UtilPoses.BlueLeftStart)
				.lineToLinearHeading(UtilPoses.BlueLeftSuspend)
				.build());

		final Pose2d samplesPot =registerTrajectory("to samples",generateSequenceBuilder(UtilPoses.BlueLeftSuspend)
				.strafeRight(24)
				.back(10)
				.turn(Math.toRadians(-90))
				.build());
		final Pose2d afterPushing = registerTrajectory("push",generateSequenceBuilder(samplesPot)
				.forward(15)
				.build());


		registerTrajectory("go decant",generateBuilder(afterPushing)
				.lineToLinearHeading(UtilPoses.BlueDecant)
				.build());

		registerTrajectory("to port",generateSequenceBuilder(UtilPoses.BlueDecant)
				.lineToLinearHeading(samplesPot)
				.build());

		registerTrajectory("park",generateBuilder(UtilPoses.BlueDecant)
				.lineToLinearHeading(UtilPoses.BlueLeftPark)
				.build());
	}

	@Override
	public void linear() {
		utils.integralLiftUpPrepare().liftSuspendHighPrepare().runAsThread();
		runTrajectory("suspend preload");
		utils.liftSuspendHigh().runCached();
		sleep(500);
		utils.openClip().liftDown().runAsThread();
		runTrajectory("to samples");
		utils.integralIntakes().runCached();
		runTrajectory("push");

		sleep(500);
		utils.armsIDLE().waitMs(1500).integralLiftUpPrepare().liftDecantHigh().runAsThread();
		runTrajectory("go decant");
		utils.decant().waitMs(1000).integralLiftDownPrepare().liftDown().integralIntakes().runAsThread();

		runTrajectory("to port");
		runTrajectory("push");
		utils.armsIDLE().waitMs(1500).integralLiftUpPrepare().liftDecantHigh().runAsThread();
		runTrajectory("go decant");
//		utils.integralIntakes().runCached();
//		sleep(1000);
//		utils.integralIntakesEnding().runCached();
//		runTrajectory("go decant");
//		utils.integralLiftUpPrepare().liftDecantHigh().runCached();
//		utils.decant();
//		utils.integralLiftDownPrepare().liftDown().runCached();
//
//		runTrajectory("intake sample3");
//		utils.integralIntakes().runCached();
//		sleep(1000);
//		utils.integralIntakesEnding().runCached();
//		runTrajectory("go decant");
//		utils.integralLiftUpPrepare().liftDecantHigh().runCached();
//		utils.decant();
//		utils.integralLiftDownPrepare().liftDown().runCached();
//
//		runTrajectory("park");
	}
}
