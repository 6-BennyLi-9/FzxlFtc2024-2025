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

		registerTrajectory("push sample 1",generateBuilder(UtilPoses.RightSuspend)
				.lineToLinearHeading(UtilPoses.RightSample1)
				.build());
		registerTrajectory("push sample 2",generateBuilder(UtilPoses.RightSample1)
				.lineToLinearHeading(UtilPoses.RightSample2)
				.build());
		registerTrajectory("push sample 3",generateBuilder(UtilPoses.RightSample2)
				.lineToLinearHeading(UtilPoses.RightSample3)
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
		sleep(500);
		utils.openClip().liftDown().integralIntakes().runAsThread();

		utils.openClaw().displayArms().runAsThread();
		runTrajectory("push sample 1");
		utils.closeClaw().armsIDLE().waitMs(1000).openClaw().decant().runAsThread();

		runTrajectory("get sample");
		sleep(500);
		utils.closeClip().runCached();
		utils.integralLiftUpPrepare().liftSuspendHighPrepare().runAsThread();
		runTrajectory("suspend 1");
		utils.liftSuspendHigh().runCached();
		sleep(500);
		utils.openClip().liftDown().integralIntakes().runAsThread();

//		utils.openClaw().displayArms().runAsThread();
//		runTrajectory("push sample 2");
//		utils.closeClaw().armsIDLE().waitMs(1000).openClaw().decant().runAsThread();
//
//		runTrajectory("get sample");
//		sleep(500);
//		utils.closeClip().runCached();
//		utils.integralLiftUpPrepare().liftSuspendHighPrepare().runAsThread();
//		runTrajectory("suspend 2");
//		utils.liftSuspendHigh().runCached();
//		sleep(500);
//		utils.openClip().liftDown().integralIntakes().runAsThread();
//
//		utils.openClaw().displayArms().runAsThread();
//		runTrajectory("push sample 3");
//		utils.closeClaw().armsIDLE().waitMs(1000).openClaw().decant().runAsThread();
//
//		runTrajectory("get sample");
//		sleep(500);
//		utils.closeClip().runCached();
//		utils.integralLiftUpPrepare().liftSuspendHighPrepare().runAsThread();
//		runTrajectory("suspend 3");
//		utils.liftSuspendHigh().runCached();
//		sleep(500);
//		utils.openClip().liftDown().integralIntakes().runAsThread();

//		utils.decant().runAsThread();
//		runTrajectory("park");
	}
}