package org.firstinspires.ftc.opmodes.autonomous;

import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.GetSample;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.RightGetFirstSample;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.RightGetSecondSample;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.RightStart;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.RightSuspend;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.acmerobotics.roadrunner.trajectorysequence.TrajectorySequence;
import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.Actions;
import org.firstinspires.ftc.cores.eventloop.StructuralLinearMode;
import org.firstinspires.ftc.cores.structure.SimpleDriveOp;

@Autonomous(preselectTeleOp = "19419", group = "1_Beta")
public class Right2 extends StructuralLinearMode {
	public static double scaleGetPosition = 0.238;
	
	@Override
	public void linear() {
		drive.setPoseEstimate(RightStart);
		client.addData("初始化点位", "_ _ |<");

		Trajectory suspend_preload=drive.trajectoryBuilder(RightStart).lineToLinearHeading(RightSuspend).build();

		Trajectory get_sample_1=drive.trajectoryBuilder(RightSuspend).lineToLinearHeading(RightGetFirstSample).build();
		Trajectory get_sample_2=drive.trajectoryBuilder(RightGetFirstSample).lineToLinearHeading(RightGetSecondSample).build();

		TrajectorySequence get_sample_suspend_1=drive.trajectorySequenceBuilder(RightGetSecondSample).lineToSplineHeading(GetSample.plus(new Pose2d(0, - 5))).build();
		TrajectorySequence get_sample_suspend_2=drive.trajectorySequenceBuilder(RightSuspend).lineToSplineHeading(GetSample.plus(new Pose2d(0, - 5))).build();

		TrajectorySequence suspend_got_sample_1=drive.trajectorySequenceBuilder(GetSample).lineToLinearHeading(RightSuspend.plus(new Pose2d(5, 5))).build();
		TrajectorySequence suspend_got_sample_2=drive.trajectorySequenceBuilder(GetSample).lineToLinearHeading(RightSuspend.plus(new Pose2d(10, 5))).build();

		Trajectory park=drive.trajectoryBuilder(RightSuspend.plus(new Pose2d(10, 5)).plus(new Pose2d(0, - 5.1))).lineToLinearHeading(GetSample).build();

		Action pre_suspend=utils.integralLiftUpPrepare().liftSuspendHighPrepare().pack();
		Action suspend_and_pre_get=utils.liftSuspendHigh().waitMs(300).openClip().waitMs(100).liftDown().integralIntakes().scaleOperate(scaleGetPosition).pack();
		Action intake=utils.displayArms().waitMs(600).closeClaw().waitMs(300).integralIntakesEnding().pack();
		Action decant_intake=utils.openClaw().waitMs(100).closeClaw().waitMs(100).openClaw().waitMs(200).armsToSafePosition().decant().pack();
		Action get_suspend_and_prepare=utils.boxRst().addAction(SimpleDriveOp.build(0, - 0.2, 0)).waitMs(800).closeClip().waitMs(1000).integralLiftUpPrepare().liftSuspendHighPrepare().pack();
		Action suspend=utils.liftSuspendHigh().waitMs(300).openClip().waitMs(100).liftDown().pack();
		Action pre_park=utils.liftSuspendHigh().waitMs(300).openClip().waitMs(100).liftDown().integralIntakes().pack();

		waitForStart();

		Actions.runThreadingAction(pre_suspend);
		drive.followTrajectory(suspend_preload);
		Actions.runThreadingAction(suspend_and_pre_get);
		sleep(600);

		drive.followTrajectory(get_sample_1);
		Actions.runThreadingAction(intake);

		sleep(900);
		Actions.runThreadingAction(decant_intake);
		drive.followTrajectory(get_sample_2);
		sleep(200);
		Actions.runThreadingAction(intake);

		sleep(900);
		Actions.runThreadingAction(decant_intake);
		sleep(900);

		drive.followTrajectorySequence(get_sample_suspend_1);
		sleep(500);
		Actions.runThreadingAction(get_suspend_and_prepare);
		sleep(1500);
		utils.rstMotors();

		drive.followTrajectorySequence(suspend_got_sample_1);
		angleCalibration(0, RightSuspend.plus(new Pose2d(5, 5)));
		drive.followTrajectory(drive.trajectoryBuilder(RightSuspend.plus(new Pose2d(5, 5))).back(3.5).build());
		Actions.runThreadingAction(suspend);
		sleep(500);
		drive.followTrajectorySequence(get_sample_suspend_2);
		sleep(500);
		Actions.runThreadingAction(get_suspend_and_prepare);
		sleep(1500);
		utils.rstMotors();

		drive.followTrajectorySequence(suspend_got_sample_2);
		angleCalibration(0, RightSuspend.plus(new Pose2d(10, 5)));
		drive.followTrajectory(drive.trajectoryBuilder(RightSuspend.plus(new Pose2d(10, 5))).back(3.2).build());
		Actions.runThreadingAction(pre_park);
		sleep(500);

		drive.followTrajectory(park);

		flagging_op_complete();
	}
}
