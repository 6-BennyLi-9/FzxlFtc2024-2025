package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.teamcode.autonomous.UtilPoses.*;

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
				.turn(90)
				.build());
		registerTrajectory("turn 2",generateSequenceBuilder(RightSample2)
				.turn(90)
				.build());
		registerTrajectory("turn 3",generateSequenceBuilder(RightSample3)
				.turn(90)
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
		utils.liftSuspendHigh().runCached();
		sleep(400);
		utils.openClip().waitMs(100).liftDown()
				.integralIntakes().scaleOperate(0.82).runAsThread();


		flagging_op_complete();
	}
}