package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Right(3悬挂+1夹取+1预载)", preselectTeleOp = "19419", group = "0_Main")
public class RightSuspendAndTake extends RightSuspend{
	Pose2d cache;
	@Override
	public void initialize() {
		super.initialize();
		//override
		cache=registerTrajectory("park",generateBuilder(UtilPoses.RightSuspend)
				.lineToLinearHeading(UtilPoses.GetSample.plus(new Pose2d(2,-15)))
				.build());

		registerTrajectory("lst park",generateSequenceBuilder(cache)
				.back(5)
				.build());
	}

	@Override
	public void linear() {
		super.linear();
//		angleCalibration(180,cache);
		utils.integralIntakes().scaleOperate(0.9).displayArms().waitMs(600).integralIntakesEnding()
				.waitMs(1200)
				.openClaw().waitMs(100).closeClaw().waitMs(100)
				.openClaw()
				.armsToSafePosition().decant()
				.runAsThread();
		sleep(800);
		runTrajectory("lst park");

		flagging_op_complete();
	}
}
