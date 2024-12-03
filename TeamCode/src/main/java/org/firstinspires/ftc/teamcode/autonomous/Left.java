package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.teamcode.autonomous.UtilPoses.Decant;
import static org.firstinspires.ftc.teamcode.autonomous.UtilPoses.LeftParkPrepare;
import static org.firstinspires.ftc.teamcode.autonomous.UtilPoses.LeftSample;
import static org.firstinspires.ftc.teamcode.autonomous.UtilPoses.LeftStart;
import static org.firstinspires.ftc.teamcode.autonomous.UtilPoses.LeftSuspend;

import static java.lang.Math.*;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.structure.SimpleDriveOp;
import org.firstinspires.ftc.teamcode.util.ops.IntegralAutonomous;

@Autonomous(preselectTeleOp = "19419", group = "0_Main")
public class Left extends IntegralAutonomous {
	@Override
	public void initialize() {
		drive.setPoseEstimate(LeftStart);
		client.addData("初始化点位",">| _ _");

		registerTrajectory("suspend preload",generateBuilder(LeftStart)
				.lineToLinearHeading(LeftSuspend)
				.build());

		registerTrajectory("get sample",generateBuilder(LeftSuspend)
				.lineToLinearHeading(LeftSample)
				.build());
		registerTrajectory("to sample 1",generateBuilder(Decant)
				.lineToLinearHeading(LeftSample.plus(new Pose2d(0,0, toRadians(-23))))
				.build());
		registerTrajectory("to sample 2",generateBuilder(Decant)
				.lineToLinearHeading(LeftSample.plus(new Pose2d(0,0, toRadians(23))))
				.build());

		registerTrajectory("decant 1",generateBuilder(LeftSample)
				.lineToLinearHeading(Decant)
				.build());
		registerTrajectory("decant 2",generateBuilder(LeftSample.plus(new Pose2d(0,0, toRadians(-23))))
				.lineToLinearHeading(Decant)
				.build());
		registerTrajectory("decant 3",generateBuilder(LeftSample.plus(new Pose2d(0,0, toRadians(23.5))))
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
		sleep(500);
		utils.openClip().waitMs(100).liftDown()
				.integralIntakes().scaleOperate(0.85).runAsThread();

		runTrajectory("get sample");
		utils.displayArms().waitMs(600).integralIntakesEnding().waitMs(1200)
				.openClaw().waitMs(100).closeClaw().waitMs(100)
				.openClaw()
				.waitMs(200)
				.integralLiftUpPrepare().liftDecantHigh()
				.runAsThread();
		sleep(1000);
		runTrajectory("decant 1");

		sleep(600);
		utils.decant().waitMs(1300).integralLiftDownPrepare().liftDown()
				.waitMs(500).integralIntakes().rotateRightTurn(0.11).scaleOperate(0.91)
				.runAsThread();
		sleep(1000);
		runTrajectory("to sample 1");

		sleep(1000);

		utils.displayArms().waitMs(600).integralIntakesEnding().waitMs(1200)
				.openClaw().waitMs(100).closeClaw().waitMs(100)
				.openClaw()
				.waitMs(200)
				.integralLiftUpPrepare().liftDecantHigh()
				.runAsThread();
		sleep(1000);
		runTrajectory("decant 2");

		sleep(600);
		utils.decant().waitMs(1300).integralLiftDownPrepare().liftDown()
				.waitMs(500).integralIntakes().rotateRightTurn(-0.11).scaleOperate(0.88)
				.runAsThread();
		sleep(1000);
		runTrajectory("to sample 2");

		sleep(1000);

		utils.displayArms().waitMs(600).integralIntakesEnding().waitMs(1200)
				.openClaw().waitMs(100).closeClaw().waitMs(100)
				.openClaw()
				.waitMs(200)
				.integralLiftUpPrepare().liftDecantHigh()
				.runAsThread();
		sleep(1000);
		runTrajectory("decant 3");

		sleep(600);
		utils.decant().waitMs(1300).integralLiftDownPrepare().liftDown()
				.liftSuspendLv1()
				.runAsThread();

		sleep(1000);
		runTrajectory("park");
		flagging_op_complete();
		utils.addAction(SimpleDriveOp.build(0,-0.25,0)).runCached();
	}
}
