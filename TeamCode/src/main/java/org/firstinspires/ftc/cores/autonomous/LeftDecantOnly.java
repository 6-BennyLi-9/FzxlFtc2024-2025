package org.firstinspires.ftc.cores.autonomous;

import static org.firstinspires.ftc.cores.autonomous.UtilPoses.Decant;
import static org.firstinspires.ftc.cores.autonomous.UtilPoses.LeftParkPrepare;
import static org.firstinspires.ftc.cores.autonomous.UtilPoses.LeftSample;
import static org.firstinspires.ftc.cores.autonomous.UtilPoses.LeftStart;
import static org.firstinspires.ftc.cores.autonomous.UtilPoses.LeftSuspend;
import static java.lang.Math.toRadians;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.cores.structure.SimpleDriveOp;
import org.firstinspires.ftc.cores.eventloop.IntegralAutonomous;

@Config
@Autonomous(name = "Left(只倒)", preselectTeleOp = "19419", group = "0_Main")
public class LeftDecantOnly extends IntegralAutonomous {
	@Override
	public void initialize() {
		drive.setPoseEstimate(LeftStart);
		client.addData("初始化点位", ">| _ _");

		registerTrajectory("decant preload", generateBuilder(LeftStart).lineToLinearHeading(Decant).build());

		registerTrajectory("get sample", generateBuilder(LeftSuspend).lineToLinearHeading(LeftSample).build());
		registerTrajectory("to sample 1", generateBuilder(Decant).lineToLinearHeading(LeftSample.plus(new Pose2d(0, 0, toRadians(- 23)))).build());
		registerTrajectory("to sample 2", generateBuilder(Decant).lineToLinearHeading(LeftSample.plus(new Pose2d(0, 0, toRadians(21.7)))).build());

		registerTrajectory("decant 1", generateBuilder(LeftSample).lineToLinearHeading(Decant).build());
		registerTrajectory("decant 2", generateBuilder(LeftSample.plus(new Pose2d(0, 0, toRadians(- 23)))).lineToLinearHeading(Decant).build());
		registerTrajectory("decant 3", generateBuilder(LeftSample.plus(new Pose2d(0, 0, toRadians(21.2)))).lineToLinearHeading(Decant).build());

		registerTrajectory("park", generateSequenceBuilder(Decant).lineToLinearHeading(LeftParkPrepare).back(15).build());
	}

	public static double scaleGetPosition1 = 0.84, scaleGetPosition2 = 0.915, scaleGetPosition3 = 0.9;

	@Override
	public void linear() {
		utils.integralLiftUpPrepare().liftDecantHigh().runAsThread();
		runTrajectory("decant preload");
		utils.decant().waitMs(100).liftDown().integralIntakes().scaleOperate(scaleGetPosition1).runAsThread();

		runTrajectory("get sample");
		utils.displayArms().waitMs(600).integralIntakesEnding().waitMs(1200).openClaw().waitMs(100).closeClaw().waitMs(100).openClaw().waitMs(200).integralLiftUpPrepare().liftDecantHigh().runAsThread();
		sleep(1000);
		runTrajectory("decant 1");

		sleep(1200);
		utils.decant().waitMs(1300).integralLiftDownPrepare().waitMs(500).liftDown().waitMs(500).integralIntakes().rotateRightTurn(0.1).scaleOperate(scaleGetPosition2).runAsThread();
		sleep(1000);
		runTrajectory("to sample 1");

		sleep(1000);

		utils.displayArms().waitMs(600).integralIntakesEnding().waitMs(1200).openClaw().waitMs(100).closeClaw().waitMs(100).openClaw().waitMs(200).integralLiftUpPrepare().liftDecantHigh().runAsThread();
		sleep(1000);
		runTrajectory("decant 2");

		sleep(1200);
		utils.decant().waitMs(1300).integralLiftDownPrepare().waitMs(500).liftDown().waitMs(500).integralIntakes().rotateRightTurn(- 0.1).displayArms().waitMs(200).scaleOperate(scaleGetPosition3).runAsThread();
		sleep(1500);
		runTrajectory("to sample 2");

		sleep(1000);

		utils.integralIntakesEnding().waitMs(1200).openClaw().waitMs(100).closeClaw().waitMs(100).openClaw().waitMs(200).integralLiftUpPrepare().liftDecantHigh().runAsThread();
		sleep(1000);
		runTrajectory("decant 3");

		sleep(1200);
		utils.decant().waitMs(1300).integralLiftDownPrepare().waitMs(500).liftDown().liftSuspendLv1().runAsThread();

		sleep(1000);
		runTrajectory("park");
		flagging_op_complete();
		utils.addAction(SimpleDriveOp.build(0, - 0.25, 0)).runCached();
	}
}
