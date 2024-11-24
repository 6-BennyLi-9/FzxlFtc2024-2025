package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.utils.IntegralLinearOp;

@Autonomous(preselectTeleOp = "19419",group = "0_main")
public class BlueLeft extends IntegralLinearOp {
	Pose2d samplesPot,afterPushing;
	@Override
	public void initialize() {
		drive.setPoseEstimate(UtilPoses.BlueLeftStart);

		registerTrajectory("suspend preload",generateBuilder(UtilPoses.BlueLeftStart)
				.lineToLinearHeading(UtilPoses.BlueLeftSuspend)
				.build());

		samplesPot =registerTrajectory("to samples",generateSequenceBuilder(UtilPoses.BlueLeftSuspend)
				.strafeRight(24)
				.back(9)
				.build());
		samplesPot=samplesPot.plus(new Pose2d(0,0,Math.toRadians(-90)));
		afterPushing = registerTrajectory("push",generateSequenceBuilder(samplesPot)
				.forward(13)
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
		utils.openClip().liftDown().integralIntakes().runAsThread();
		runTrajectory("to samples");

		angleCalibration(-90,samplesPot);
		runTrajectory("push");
		utils.armsIDLE()
				.waitMs(1900)
				.stopIO().integralLiftUpPrepare().liftDecantHigh().runAsThread();
		runTrajectory("go decant");
		utils.decant().runCached();
		sleep(950);

		utils.boxRst().liftDown().integralIntakes().runAsThread();
		runTrajectory("to port");
		angleCalibration(-90,samplesPot);
		utils.armsIDLE().scalesProbe().
				waitMs(1900)
				.scalesBack().stopIO().integralLiftUpPrepare().liftDecantHigh()
				.runAsThread();
		runTrajectory("go decant");
		utils.decant().runCached();
		sleep(950);

		utils.boxRst().liftDown().integralIntakes().runAsThread();
		runTrajectory("to port");
		angleCalibration(-90,samplesPot);
		utils.armsIDLE().scalesProbe()
				.waitMs(1900)
				.scalesBack().stopIO().integralLiftUpPrepare().liftDecantHigh()
				.runAsThread();
		runTrajectory("go decant");
		utils.decant().runCached();
		sleep(950);
		utils.boxRst().liftDown().runAsThread();

		utils.decant().runAsThread();
		runTrajectory("park");
	}
}
