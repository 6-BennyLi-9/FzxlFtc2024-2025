package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.teamcode.autonomous.UtilPoses.GetSample;
import static org.firstinspires.ftc.teamcode.autonomous.UtilPoses.RightStart;
import static org.firstinspires.ftc.teamcode.autonomous.UtilPoses.RightSuspend;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.util.ops.IntegralAutonomous;

@Autonomous(name = "Right(3悬挂)", preselectTeleOp = "19419", group = "0_Main")
public class RightSuspend extends IntegralAutonomous {
	@Override
	public void initialize() {
		drive.setPoseEstimate(RightStart);
		client.addData("初始化点位","_ _ |<");

		registerTrajectory("suspend preload",generateBuilder(RightStart)
				.lineToLinearHeading(RightSuspend)
				.build());

		registerTrajectory("get sample suspend",generateSequenceBuilder(RightSuspend)
				.lineToLinearHeading(GetSample.plus(new Pose2d(0,-5)))
				.back(5)
				.build());

		registerTrajectory("suspend 1",generateSequenceBuilder(GetSample)
				.lineToLinearHeading(RightSuspend.plus(new Pose2d(5,5)))
				.back(5.2)
				.build());

		registerTrajectory("suspend 2",generateSequenceBuilder(GetSample)
				.lineToLinearHeading(RightSuspend.plus(new Pose2d(5,5)))
				.back(5.2)
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

		runTrajectory("get sample suspend");
		sleep(1000);
		utils.closeClip().waitMs(800).liftSuspendHighPrepare().runAsThread();

		sleep(800);
		runTrajectory("suspend 1");
		utils.liftSuspendHigh().waitMs(300).openClip().waitMs(100).liftDown().runAsThread();
		sleep(600);

		runTrajectory("get sample suspend");
		sleep(1000);
		utils.closeClip().waitMs(800).liftSuspendHighPrepare().runAsThread();

		sleep(800);
		runTrajectory("suspend 2");
		utils.liftSuspendHigh().waitMs(300).openClip().waitMs(100).liftDown().runAsThread();
		sleep(600);

		runTrajectory("park");
		flagging_op_complete();
	}
}
