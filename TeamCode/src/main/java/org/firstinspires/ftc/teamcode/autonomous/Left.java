package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.teamcode.autonomous.UtilPoses.*;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.util.ops.IntegralAutonomous;

@Autonomous(preselectTeleOp = "19419", group = "0_Main")
public class Left extends IntegralAutonomous {
	@Override
	public void initialize() {
		drive.setPoseEstimate(LeftStart);

		registerTrajectory("suspend preload",generateBuilder(LeftStart)
				.lineToLinearHeading(LeftSuspend)
				.build());

		registerTrajectory("get sample",generateBuilder(LeftSuspend)
				.lineToLinearHeading(LeftSample)
				.build());
		registerTrajectory("to sample 1",generateBuilder(Decant)
				.lineToLinearHeading(LeftSample.plus(new Pose2d(0,0,Math.toRadians(-23))))
				.build());
		registerTrajectory("to sample 2",generateBuilder(Decant)
				.lineToLinearHeading(LeftSample.plus(new Pose2d(0,0,Math.toRadians(23.5))))
				.build());

		registerTrajectory("decant",generateBuilder(LeftSample)
				.lineToLinearHeading(Decant)
				.build());

		registerTrajectory("park",generateSequenceBuilder(Decant)
				.lineToLinearHeading(LeftParkPrepare)
				.back(15)
				.build());
	}

	@Override
	public void linear() {
		utils.integralLiftUpPrepare().liftSuspendHighPrepare().runAsThread();
		runTrajectory("suspend preload");
		utils.liftSuspendHigh().runCached();
		sleep(400);
		utils.openClip().waitMs(100).liftDown()
				.integralIntakes().scaleOperate(0.81).runAsThread();

		runTrajectory("get sample");
		utils.displayArms().waitMs(600).integralIntakesEnding().waitMs(1500)
//				.openClaw().waitMs(100).closeClaw().waitMs(100)
//				.openClaw().waitMs(100).closeClaw().waitMs(100)
//				.waitMs(400)
				.openClaw()
				.integralLiftUpPrepare().liftDecantHigh()
				.runAsThread();
		sleep(1000);
		runTrajectory("decant");

		sleep(1000);
		utils.decant().waitMs(1300).integralLiftDownPrepare().liftDown()
				.integralIntakes().rotateRightTurn(0.135).scaleOperate(0.87)
				.runAsThread();
		sleep(1000);
		runTrajectory("to sample 1");

		sleep(1000);

		utils.displayArms().waitMs(600).integralIntakesEnding().waitMs(1500)
//				.openClaw().waitMs(100).closeClaw().waitMs(100)
//				.openClaw().waitMs(100).closeClaw().waitMs(100)
//				.waitMs(400)
				.openClaw()
				.integralLiftUpPrepare().liftDecantHigh()
				.runAsThread();
		sleep(1000);
		runTrajectory("decant");
		sleep(1000);
		utils.decant().waitMs(1300).integralLiftDownPrepare().liftDown()
				.waitMs(500).integralIntakes().rotateRightTurn(-0.135).scaleOperate(0.86)
				.runAsThread();
		sleep(1000);
		runTrajectory("to sample 2");

		sleep(1000);

		utils.displayArms().waitMs(600).integralIntakesEnding().waitMs(1500)
//				.openClaw().waitMs(100).closeClaw().waitMs(100)
//				.openClaw().waitMs(100).closeClaw().waitMs(100)
//				.waitMs(400)
				.openClaw()
				.integralLiftUpPrepare().liftDecantHigh()
				.runAsThread();
		sleep(1000);
		runTrajectory("decant");
		sleep(1000);
		utils.decant().waitMs(1300).integralLiftDownPrepare().liftDown()
				.liftSuspendLv1()
				.runAsThread();

		sleep(1000);
		runTrajectory("park");
		flagging_op_complete();
	}
}
