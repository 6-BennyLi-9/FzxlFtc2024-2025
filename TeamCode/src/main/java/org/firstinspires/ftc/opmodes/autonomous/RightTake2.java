package org.firstinspires.ftc.opmodes.autonomous;

import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.GetSample;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.RightGetFirstSample;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.RightGetSecondSample;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.RightStart;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.RightSuspend;
import static java.lang.Math.toRadians;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.cores.structure.SimpleDriveOp;
import org.firstinspires.ftc.cores.eventloop.IntegralAutonomous;

@Config
@Autonomous(name = "Right(3悬挂+2夹取)", preselectTeleOp = "19419", group = "0_Main")
public class RightTake2 extends IntegralAutonomous {
	@Override
	public void initialize() {
		drive.setPoseEstimate(RightStart);
		client.addData("初始化点位", "_ _ |<");

		registerTrajectory("suspend preload", generateBuilder(RightStart).lineToLinearHeading(RightSuspend).build());

		registerTrajectory("get sample 1", generateBuilder(RightSuspend).lineToLinearHeading(RightGetFirstSample).build());
		registerTrajectory("get sample 2", generateBuilder(RightGetFirstSample).lineToLinearHeading(RightGetSecondSample).build());

		registerTrajectory("get sample suspend 1", generateSequenceBuilder(RightGetSecondSample).lineToSplineHeading(GetSample.plus(new Pose2d(0, - 5))).build());
		registerTrajectory("get sample suspend 2", generateSequenceBuilder(RightSuspend).lineToSplineHeading(GetSample.plus(new Pose2d(0, - 5))).build());

		registerTrajectory("suspend got sample 1", generateSequenceBuilder(GetSample).lineToLinearHeading(RightSuspend.plus(new Pose2d(5, 5))).build());
		registerTrajectory("suspend got sample 2", generateSequenceBuilder(GetSample).lineToLinearHeading(RightSuspend.plus(new Pose2d(10, 5))).build());

		registerTrajectory("park", generateBuilder(RightSuspend.plus(new Pose2d(10, 5)).plus(new Pose2d(0,-5.1))).lineToLinearHeading(GetSample.plus(new Pose2d(0, 0, toRadians(- 90)))).build());
	}

	public static double scaleGetPosition = 0.84;

	@Override
	public void linear() {
		utils.integralLiftUpPrepare().liftSuspendHighPrepare().runAsThread();
		runTrajectory("suspend preload");
		utils.liftSuspendHigh().waitMs(300).openClip().waitMs(100).liftDown().integralIntakes().scaleOperate(scaleGetPosition).runAsThread();
		sleep(600);

		runTrajectory("get sample 1");
		utils.displayArms().waitMs(600).closeClaw().waitMs(300).integralIntakesEnding().runCached();

		sleep(900);
		utils.openClaw().waitMs(100).closeClaw().waitMs(100).openClaw().waitMs(200).scaleOperate(scaleGetPosition).armsToSafePosition().waitMs(200).decant().runAsThread();
		runTrajectory("get sample 2");
		sleep(200);
		utils.displayArms().waitMs(600).closeClaw().waitMs(300).integralIntakesEnding().runCached();

		sleep(900);
		utils.openClaw().waitMs(100).closeClaw().waitMs(100).openClaw().waitMs(200).armsToSafePosition().decant().runAsThread();
		sleep(900);

		/*
		*
		registerTrajectory("suspend got sample 1", generateSequenceBuilder(GetSample).lineToLinearHeading(RightSuspend.plus(new Pose2d(5, 5))).build());
		registerTrajectory("suspend got sample 2", generateSequenceBuilder(GetSample).lineToLinearHeading(RightSuspend.plus(new Pose2d(10, 5))).build());
		*
		* */

		runTrajectory("get sample suspend 1");
		sleep(500);
		utils.boxRst().addAction(SimpleDriveOp.build(0, - 0.2, 0)).waitMs(800).closeClip().waitMs(1000).integralLiftUpPrepare().liftSuspendHighPrepare().runAsThread();
		sleep(1500);
		utils.rstMotors();

		runTrajectory("suspend got sample 1");
		utils	.addStatement(()->angleCalibration(0,RightSuspend.plus(new Pose2d(5, 0))))
				.addAction(SimpleDriveOp.build(0, - 0.25, 0)).waitMs(500).liftSuspendHigh().waitMs(300).openClip().waitMs(100).liftDown().integralIntakes().runAsThread();
		sleep(500);
		runTrajectory("get sample suspend 2");
		sleep(500);
		utils.addAction(SimpleDriveOp.build(0, - 0.2, 0)).waitMs(800).closeClip().waitMs(1000).integralLiftUpPrepare().liftSuspendHighPrepare().runAsThread();
		sleep(1500);
		utils.rstMotors();

		runTrajectory("suspend got sample 2");
		utils	.addStatement(()->angleCalibration(0,RightSuspend.plus(new Pose2d(10, 0))))
				.addAction(SimpleDriveOp.build(0, - 0.25, 0)).waitMs(500).liftSuspendHigh().waitMs(300).openClip().waitMs(100).liftDown().integralIntakes().runAsThread();
		sleep(500);

		runTrajectory("park");

		flagging_op_complete();
	}
}
