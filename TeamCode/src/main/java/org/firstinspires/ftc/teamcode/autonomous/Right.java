package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.util.ops.IntegralAutonomous;

@Autonomous(preselectTeleOp = "19419", group = "0_Main")
public class Right extends IntegralAutonomous {
	@Override
	public void initialize() {
		drive.setPoseEstimate(UtilPoses.RightStart);

		registerTrajectory("suspend preload",generateBuilder(UtilPoses.RightStart)
				.lineToLinearHeading(UtilPoses.RightSuspend)
				.build());

		registerTrajectory("to sample 1",generateBuilder(UtilPoses.RightSuspend)
				.lineToLinearHeading(UtilPoses.RightSample)
				.build());
		registerTrajectory("turn to sample 2",generateSequenceBuilder(UtilPoses.RightSample)
				.turn(Math.toRadians(-22.5))
				.build());
		registerTrajectory("turn to sample 3",generateSequenceBuilder(UtilPoses.RightSample)
				.turn(Math.toRadians(23.5))
				.build());

		registerTrajectory("get sample",generateBuilder(UtilPoses.RightSuspend)
				.lineToLinearHeading(UtilPoses.GetSample)
				.build());

		registerTrajectory("suspend 1",generateBuilder(UtilPoses.GetSample)
				.lineToLinearHeading(UtilPoses.RightSuspend.plus(new Pose2d(5,0)))
				.build());
		registerTrajectory("suspend 2",generateBuilder(UtilPoses.GetSample)
				.lineToLinearHeading(UtilPoses.RightSuspend.plus(new Pose2d(10,0)))
				.build());
		registerTrajectory("suspend 3",generateBuilder(UtilPoses.GetSample)
				.lineToLinearHeading(UtilPoses.RightSuspend.plus(new Pose2d(15,0)))
				.build());


		registerTrajectory("park",generateBuilder(UtilPoses.RightSuspend)
				.lineToLinearHeading(UtilPoses.GetSample)
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

		runTrajectory("to sample 1");
		utils.integralIntakesEnding().waitMs(1000)
				.openClaw().waitMs(100).closeClaw().waitMs(100)
				.openClaw().waitMs(100).closeClaw().waitMs(100)
				.openClaw()
				.armsToSafePosition().decant()
				.runCached();

		runTrajectory("turn to sample 2");
		utils.scaleOperate(0.89).integralIntakes().rotateRightTurn(0.125).scaleOperate(0.86)
				.waitMs(1000)
				.integralIntakesEnding().waitMs(1000)
				.openClaw().waitMs(100).closeClaw().waitMs(100)
				.openClaw().waitMs(100).closeClaw().waitMs(100)
				.openClaw()
				.armsToSafePosition().decant()
				.runCached();


//		runTrajectory("park");
		flagging_op_complete();
	}
}