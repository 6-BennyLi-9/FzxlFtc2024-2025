package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.util.ops.IntegralAutonomous;

@Autonomous(preselectTeleOp = "19419", group = "0_Main")
public class Left extends IntegralAutonomous {

	@Override
	public void initialize() {
		drive.setPoseEstimate(UtilPoses.LeftStart);

		registerTrajectory("suspend preload",generateBuilder(UtilPoses.LeftStart)
				.lineToLinearHeading(UtilPoses.LeftSuspend)
				.build());

		registerTrajectory("get sample",generateBuilder(UtilPoses.LeftSuspend)
				.lineToSplineHeading(UtilPoses.LeftSample)
				.build());
		registerTrajectory("to sample 1",generateBuilder(UtilPoses.Decant)
				.lineToSplineHeading(UtilPoses.LeftSample.plus(new Pose2d(0,0,Math.toRadians(-25))))
				.build());

		registerTrajectory("decant",generateBuilder(UtilPoses.LeftSample)
				.lineToLinearHeading(UtilPoses.Decant)
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
		sleep(400);
		utils.openClip().waitMs(100).liftDown()
				.integralIntakes().scaleOperate(0.82).runAsThread();

		runTrajectory("get sample");
		utils.integralIntakesEnding().waitMs(1000).openClaw().closeClaw().openClaw().closeClaw().openClaw()
				.integralLiftUpPrepare().liftDecantHigh()
				.runAsThread();
		sleep(500);
		runTrajectory("decant");

		sleep(1000);
		utils.decant().waitMs(1200).integralLiftDownPrepare().liftDown()
//				.integralIntakes().scaleOperate(0.9)
				.runAsThread();
		sleep(1100);
		runTrajectory("to sample 1");
//		angleCalibration(145);

		utils.integralIntakes().rotateRightTurn(0.1).scaleOperate(0.9).runCached();
		sleep(500);
		utils.integralIntakesEnding().waitMs(1000).openClaw().runCached();

//		utils.decant().runAsThread();
//		runTrajectory("park");
		flagging_op_complete();
	}
}
