package org.firstinspires.ftc.teamcode.autonomous;

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
				.lineToSplineHeading(UtilPoses.GetSample)
				.build());

//		registerTrajectory("get sample 1",generateSequenceBuilder(UtilPoses.LeftSuspend)
//				.lineToSplineHeading(UtilPoses.LeftSample1)
////				.forward(9)
////				.turn(Math.toRadians(180))
//				.build());
//		registerTrajectory("get sample 2",generateSequenceBuilder(UtilPoses.Decant)
//				.lineTo(UtilPoses.LeftSample2.vec())
//				.turn(Math.toRadians(180))
//				.build());
//		registerTrajectory("get sample 3",generateSequenceBuilder(UtilPoses.Decant)
//				.lineTo(UtilPoses.LeftSample3.vec())
//				.turn(Math.toRadians(180))
//				.build());

		registerTrajectory("decant",generateBuilder(UtilPoses.LeftSample1)
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
		sleep(500);
		utils.openClip().liftDown().integralIntakes().openClaw().displayArms().runAsThread();

//		runTrajectory("get sample 1");
//		utils.closeClaw().armsIDLE().runCached();

//		utils.openClaw().integralLiftUpPrepare().liftDecantHigh().runAsThread();
//		runTrajectory("decant");
//		utils.decant().waitMs(1000).integralLiftDownPrepare().liftDown().runAsThread();

//		utils.openClaw().displayArms().runAsThread();
//		runTrajectory("get sample 2");
//		utils.closeClaw().runCached();
//
//		utils.openClaw().integralLiftUpPrepare().liftDecantHigh().runAsThread();
//		runTrajectory("decant");
//		utils.decant().waitMs(1000).integralLiftDownPrepare().liftDown().runAsThread();
//
//		utils.openClaw().displayArms().runAsThread();
//		runTrajectory("get sample 3");
//		utils.closeClaw().runCached();
//
//		utils.openClaw().integralLiftUpPrepare().liftDecantHigh().runAsThread();
//		runTrajectory("decant");
//		utils.decant().waitMs(1000).integralLiftDownPrepare().liftDown().runAsThread();

//		utils.decant().runAsThread();
//		runTrajectory("park");
		flagging_op_complete();
	}
}
