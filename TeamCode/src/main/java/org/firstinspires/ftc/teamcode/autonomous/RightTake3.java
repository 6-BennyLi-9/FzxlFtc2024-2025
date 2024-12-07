package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.teamcode.autonomous.UtilPoses.GetSample;
import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Right(1悬挂+2夹取)", group = "3_Specials")
public class RightTake3 extends RightTake2 {
	@Override
	public void initialize() {
		super.initialize();

		//override
		registerTrajectory("park",generateBuilder(UtilPoses.RightGetSecondSample)
				.lineToLinearHeading(GetSample.plus(new Pose2d(-10, 0, toRadians(90))))
				.build());
	}

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

		runTrajectory("park");
		sleep(1000);

		utils.boxRst().runCached();

		flagging_op_complete();
	}
}
