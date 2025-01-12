package org.firstinspires.ftc.opmodes.autonomous;

import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.GetSample;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.RightFirstSample;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.RightSecondSample;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.RightStart;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.RightSuspend;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.RightThirdSample;
import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.cores.eventloop.IntegralAutonomous;

@Disabled
@Deprecated
@Autonomous(name = "*Right(2悬挂+3夹取)", preselectTeleOp = "19419", group = "2_Betas")
public class RightTake extends IntegralAutonomous {
	@Override
	public void initialize() {
		drive.setPoseEstimate(RightStart);
		client.addData("初始化点位", "_ _ |<");

		registerTrajectory("suspend preload", generateBuilder(RightStart).lineToLinearHeading(RightSuspend).build());

		registerTrajectory("to sample 1", generateBuilder(RightSuspend).lineToLinearHeading(RightFirstSample).build());
		Pose2d cache = registerTrajectory("turn 1", generateSequenceBuilder(RightFirstSample).turn(toRadians(- 90)).build());

		registerTrajectory("to sample 2", generateBuilder(cache).lineToLinearHeading(RightSecondSample).build());
		cache = registerTrajectory("turn 2", generateSequenceBuilder(RightSecondSample).turn(toRadians(- 100)).build());

		registerTrajectory("to sample 3", generateBuilder(cache).lineToLinearHeading(RightThirdSample).build());
		Pose2d afterGet = registerTrajectory("turn 3", generateSequenceBuilder(RightThirdSample).turn(toRadians(- 100)).build());

		registerTrajectory("place to get", generateBuilder(afterGet).lineToLinearHeading(GetSample).build());
		registerTrajectory("suspend to get", generateBuilder(RightSuspend).lineToLinearHeading(GetSample).build());

		registerTrajectory("suspend 1", generateBuilder(GetSample).lineToLinearHeading(RightSuspend.plus(new Pose2d(5))).build());
		registerTrajectory("suspend 2", generateBuilder(GetSample).lineToLinearHeading(RightSuspend.plus(new Pose2d(10))).build());
		registerTrajectory("suspend 3", generateBuilder(GetSample).lineToLinearHeading(RightSuspend.plus(new Pose2d(15))).build());

		registerTrajectory("park", generateBuilder(RightSuspend).lineToLinearHeading(GetSample).build());
	}

	@Override
	public void linear() {
		utils.integralLiftUpPrepare().liftSuspendHighPrepare().runAsThread();
		runTrajectory("suspend preload");
		utils.liftSuspendHigh().waitMs(300).openClip().waitMs(100).liftDown().runAsThread();

		sleep(600);
		utils.rotateRightTurn(0.18).scaleOperate(0.755).runCached();

		runTrajectory("to sample 1");
		sleep(500);
		utils.displayArms().waitMs(600).closeClaw().waitMs(300).armsToSafePosition().scaleOperate(0.95).waitMs(500).runAsThread();
		sleep(960);
		runTrajectory("turn 1");
		utils.displayArms().waitMs(500).openClaw().waitMs(200).armsToSafePosition().scaleOperate(0.76).runAsThread();

		sleep(900);
		runTrajectory("to sample 2");
		sleep(500);
		utils.displayArms().waitMs(200).scaleOperate(0.76).waitMs(800).closeClaw().waitMs(300).armsToSafePosition().scaleOperate(0.6).waitMs(500).runAsThread();
		sleep(900);
		runTrajectory("turn 2");
		utils.scaleOperate(0.9).displayArms().waitMs(800).openClaw().waitMs(400).armsToSafePosition().scalesBack().runAsThread();


		sleep(1200);
		utils.rotateToMid().rotateRightTurn(0.2).runAsThread();
		runTrajectory("to sample 3");
		sleep(500);
		utils.displayArms().waitMs(200).scaleOperate(0.765).waitMs(800).closeClaw().waitMs(300).armsToSafePosition().scaleOperate(0.6).waitMs(500).runCached();
		sleep(900);
		runTrajectory("turn 3");
		utils.scaleOperate(0.9).displayArms().waitMs(600).openClaw().waitMs(200).armsIDLE().scalesBack().rotateToMid().runCached();

		runTrajectory("place to get");
		utils.closeClip().integralLiftUpPrepare().waitMs(300).liftSuspendHighPrepare().runAsThread();
		sleep(1000);
		runTrajectory("suspend 1");
		utils.liftSuspendHigh().waitMs(300).openClip().waitMs(100).liftDown().runAsThread();

		runTrajectory("park");
		flagging_op_complete();
	}
}