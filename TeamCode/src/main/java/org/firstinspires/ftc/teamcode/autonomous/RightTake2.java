package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.teamcode.autonomous.UtilPoses.GetSample;
import static org.firstinspires.ftc.teamcode.autonomous.UtilPoses.RightGetSample1;
import static org.firstinspires.ftc.teamcode.autonomous.UtilPoses.RightGetSample2;
import static org.firstinspires.ftc.teamcode.autonomous.UtilPoses.RightStart;
import static org.firstinspires.ftc.teamcode.autonomous.UtilPoses.RightSuspend;

import static java.lang.Math.*;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.util.ops.IntegralAutonomous;

@Autonomous(name = "Right(2悬挂+2夹取)", preselectTeleOp = "19419", group = "0_Main")
public class RightTake2 extends IntegralAutonomous {
	@Override
	public void initialize() {
		drive.setPoseEstimate(RightStart);
		client.addData("初始化点位","_ _ |<");

		registerTrajectory("suspend preload",generateBuilder(RightStart)
				.lineToLinearHeading(RightSuspend)
				.build());

		registerTrajectory("get sample 1",generateBuilder(RightSuspend)
				.lineToLinearHeading(RightGetSample1)
				.build());
		registerTrajectory("get sample 2",generateBuilder(RightGetSample1)
				.lineToLinearHeading(RightGetSample2)
				.build());

		registerTrajectory("get sample suspend",generateSequenceBuilder(RightGetSample2)
				.lineToSplineHeading(GetSample.plus(new Pose2d(0,-5)))
				.back(5.3)
				.build());

		registerTrajectory("suspend got sample 1",generateSequenceBuilder(GetSample)
				.lineToLinearHeading(RightSuspend.plus(new Pose2d(5,5)))
				.back(5.1)
				.build());
		registerTrajectory("suspend got sample 2",generateSequenceBuilder(GetSample)
				.lineToLinearHeading(RightSuspend.plus(new Pose2d(10,5)))
				.back(5.1)
				.build());

		registerTrajectory("park",generateBuilder(RightSuspend)
				.lineToLinearHeading(GetSample.plus(new Pose2d(0,0, toRadians(-90))))
				.build());
	}

	public static double scaleGetPosition=0.85;

	@Override
	public void linear() {
		utils.integralLiftUpPrepare().liftSuspendHighPrepare().runAsThread();
		runTrajectory("suspend preload");
		utils.liftSuspendHigh().waitMs(300).openClip().waitMs(100).liftDown()
				.integralIntakes().scaleOperate(scaleGetPosition)
				.runAsThread();
		sleep(600);

		runTrajectory("get sample 1");
		utils.displayArms().waitMs(600).closeClaw().waitMs(300).integralIntakesEnding().runCached();

		sleep(1000);
		utils.openClaw().waitMs(100).closeClaw().waitMs(100)
				.openClaw()
				.waitMs(200)
				.scaleOperate(scaleGetPosition).armsToSafePosition().waitMs(200).decant().runAsThread();
		runTrajectory("get sample 2");
		sleep(200);
		utils.displayArms().waitMs(600).closeClaw().waitMs(300).integralIntakesEnding().runCached();

		sleep(1000);
		utils.openClaw().waitMs(100).closeClaw().waitMs(100)
				.openClaw()
				.waitMs(200)
				.armsToSafePosition().decant().runAsThread();
		sleep(1100);
		utils.armsToSafePosition().openClaw().boxRst().runAsThread();

		runTrajectory("get sample suspend");
		sleep(500);
		utils.closeClip().waitMs(500).integralLiftUpPrepare().liftSuspendHighPrepare().runAsThread();

		runTrajectory("suspend got sample 1");
		utils.liftSuspendHigh().waitMs(300).openClip().waitMs(100).liftDown()
				.integralIntakes()
				.runAsThread();
		sleep(600);

		runTrajectory("get sample suspend");
		sleep(500);
		utils.closeClip().waitMs(500).integralLiftUpPrepare().liftSuspendHighPrepare().runAsThread();

		runTrajectory("suspend got sample 2");
		utils.liftSuspendHigh().waitMs(300).openClip().waitMs(100).liftDown()
				.integralIntakes()
				.runAsThread();
		sleep(600);

		runTrajectory("park");

		flagging_op_complete();
	}
}
