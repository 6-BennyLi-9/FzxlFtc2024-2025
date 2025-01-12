package org.firstinspires.ftc.opmodes.autonomous;

import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.GetSample;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.RightGetFirstSample;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.RightGetSecondSample;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.RightStart;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.RightSuspend;
import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.cores.eventloop.IntegralAutonomous;
import org.firstinspires.ftc.cores.structure.SimpleDriveOp;

@Deprecated
@Disabled
@Autonomous(name = "Right(1悬挂+2夹取)", group = "3_Specials")
public class RightTake3 extends IntegralAutonomous {
	public static double scaleGetPosition = 0.82;

	@Override
	public void initialize() {
		drive.setPoseEstimate(RightStart);
		client.addData("初始化点位", "_ _ |<");

		registerTrajectory("suspend preload", generateBuilder(RightStart).lineToLinearHeading(RightSuspend).build());

		registerTrajectory("get sample 1", generateBuilder(RightSuspend).lineToLinearHeading(RightGetFirstSample).build());
		registerTrajectory("get sample 2", generateBuilder(RightGetFirstSample).lineToLinearHeading(RightGetSecondSample).build());

		registerTrajectory("park", generateBuilder(UtilPoses.RightGetSecondSample).lineToLinearHeading(GetSample.plus(new Pose2d(0, 0, toRadians(180)))).build());
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
		sleep(1000);

		utils.decant().liftSuspendLv1().runAsThread();
		runTrajectory("park");
		sleep(1000);
		utils.boxRst().addAction(SimpleDriveOp.build(0, - 0.25, 0)).runCached();

		flagging_op_complete();
	}
}
