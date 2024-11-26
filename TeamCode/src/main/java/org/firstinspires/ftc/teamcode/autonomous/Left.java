package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.util.ops.IntegralAutonomous;

@Autonomous(preselectTeleOp = "19419",group = "0_Main")
public class Left extends IntegralAutonomous {
	Pose2d samplesPot,afterPushing;
	@Override
	public void initialize() {
		drive.setPoseEstimate(UtilPoses.LeftStart);

		registerTrajectory("suspend preload",generateBuilder(UtilPoses.LeftStart)
				.lineToLinearHeading(UtilPoses.LeftSuspend)
				.build());

		registerTrajectory("get sample 1",generateSequenceBuilder(UtilPoses.LeftSuspend)
				.lineTo(UtilPoses.LeftSample1.vec())
				.turn(Math.toRadians(180))
				.build());

		registerTrajectory("park",generateBuilder(UtilPoses.Decant)
				.lineToLinearHeading(UtilPoses.LeftPark)
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
				.openClaw().integralLiftUpPrepare().liftDecantHigh().runAsThread();
		runTrajectory("go decant");
		utils.decant().runCached();
		sleep(950);

		utils.boxRst().liftDown().integralIntakes().runAsThread();
		runTrajectory("to port");
		angleCalibration(-90,samplesPot);
		utils.armsIDLE().scalesProbe().
				waitMs(1900)
				.scalesBack().openClaw().integralLiftUpPrepare().liftDecantHigh()
				.runAsThread();
		runTrajectory("go decant");
		utils.decant().runCached();
		sleep(950);

		utils.boxRst().liftDown().integralIntakes().runAsThread();
		runTrajectory("to port");
		angleCalibration(-90,samplesPot);
		utils.armsIDLE().scalesProbe()
				.waitMs(1900)
				.scalesBack().openClaw().integralLiftUpPrepare().liftDecantHigh()
				.runAsThread();
		runTrajectory("go decant");
		utils.decant().runCached();
		sleep(950);
		utils.boxRst().liftDown().runAsThread();

		utils.decant().runAsThread();
		runTrajectory("park");
	}
}
