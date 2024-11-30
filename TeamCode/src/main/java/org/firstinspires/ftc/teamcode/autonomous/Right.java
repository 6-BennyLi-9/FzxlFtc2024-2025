package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.teamcode.autonomous.UtilPoses.GetSample;
import static org.firstinspires.ftc.teamcode.autonomous.UtilPoses.RightSample;
import static org.firstinspires.ftc.teamcode.autonomous.UtilPoses.RightSample1;
import static org.firstinspires.ftc.teamcode.autonomous.UtilPoses.RightSample2;
import static org.firstinspires.ftc.teamcode.autonomous.UtilPoses.RightSample3;
import static org.firstinspires.ftc.teamcode.autonomous.UtilPoses.RightStart;
import static org.firstinspires.ftc.teamcode.autonomous.UtilPoses.RightSuspend;
import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.util.ops.IntegralAutonomous;

@Autonomous(preselectTeleOp = "19419", group = "0_Main")
public class Right extends IntegralAutonomous {
	@Override
	public void initialize() {
		drive.setPoseEstimate(RightStart);
		client.addData("初始化点位","_ _ |<");

		registerTrajectory("suspend preload",generateBuilder(RightStart)
				.lineToLinearHeading(RightSuspend)
				.build());

		registerTrajectory("to sample 1",generateBuilder(RightSuspend)
				.lineToLinearHeading(RightSample1)
				.build());
		registerTrajectory("to sample 2",generateBuilder(RightSample1)
				.lineToLinearHeading(RightSample2)
				.build());
		registerTrajectory("to sample 3",generateBuilder(RightSample2)
				.lineToLinearHeading(RightSample3)
				.build());

		registerTrajectory("turn 1",generateSequenceBuilder(RightSample1)
				.turn(toRadians(-90))
				.build());
		registerTrajectory("turn 2",generateSequenceBuilder(RightSample2)
				.turn(toRadians(-90))
				.build());
		registerTrajectory("turn 3",generateSequenceBuilder(RightSample3)
				.turn(toRadians(-90))
				.build());

		registerTrajectory("get sample",generateBuilder(RightSample)
				.lineToLinearHeading(GetSample)
				.build());

		registerTrajectory("suspend 1",generateBuilder(GetSample)
				.lineToLinearHeading(RightSuspend.plus(new Pose2d(5,0)))
				.build());
		registerTrajectory("suspend 2",generateBuilder(GetSample)
				.lineToLinearHeading(RightSuspend.plus(new Pose2d(10,0)))
				.build());
		registerTrajectory("suspend 3",generateBuilder(GetSample)
				.lineToLinearHeading(RightSuspend.plus(new Pose2d(15,0)))
				.build());

		registerTrajectory("park",generateBuilder(RightSuspend)
				.lineToLinearHeading(GetSample)
				.build());
	}

	@Override
	public void linear() {
		utils.integralLiftUpPrepare().liftSuspendHighPrepare().runAsThread();
		runTrajectory("suspend preload");
		utils.liftSuspendHigh().waitMs(300).openClip().waitMs(100).liftDown().runAsThread();

		sleep(600);
		utils.scaleOperate(0.76).rotateRightTurn(0.2).runCached();

		runTrajectory("to sample 1");
		sleep(500);
		utils.displayArms().waitMs(600).closeClaw().waitMs(300)
				.armsIDLE().scaleOperate(0.95).waitMs(500).runAsThread();
		sleep(660);
		runTrajectory("turn 1");
		utils.displayArms().waitMs(500).openClaw().waitMs(200).scaleOperate(0.76).armsIDLE().runAsThread();

		sleep(1000);
		runTrajectory("to sample 2");
		sleep(500);
		utils.displayArms().waitMs(800).closeClaw().waitMs(300)
				.armsIDLE().scaleOperate(0.6).waitMs(500).runAsThread();
		sleep(880);
		runTrajectory("turn 2");
		utils.scaleOperate(0.9)
				.displayArms().waitMs(600).openClaw().waitMs(500).scaleOperate(0.77).armsIDLE().runAsThread();


		sleep(1200);
		runTrajectory("to sample 3");
		sleep(500);
		utils.displayArms().waitMs(800).closeClaw().waitMs(300)
				.armsIDLE().scaleOperate(0.6).waitMs(500).runAsThread();
		sleep(880);
		runTrajectory("turn 3");
		utils.scaleOperate(0.9)
				.displayArms().waitMs(600).openClaw().waitMs(200)
				.armsIDLE().scalesBack().armsIDLE().rotateToMid().runCached();

		runTrajectory("get sample");
//		utils.closeClaw();
//		runTrajectory("turn 1");
//		utils.openClaw();

		flagging_op_complete();
	}
}