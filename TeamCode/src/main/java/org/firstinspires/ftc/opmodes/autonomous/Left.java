package org.firstinspires.ftc.opmodes.autonomous;

import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.Decant;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.LeftParkPrepare;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.LeftSample;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.LeftStart;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.LeftSuspend;
import static java.lang.Math.toRadians;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.acmerobotics.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.cores.eventloop.StructuralLinearMode;
import org.firstinspires.ftc.cores.structure.SimpleDriveOp;

@Config
@Autonomous(preselectTeleOp = "19419", group = "0_Main")
public class Left extends StructuralLinearMode {
	public static double scaleGetPosition1 = 0.238, scaleGetPosition2 = 0.2905, scaleGetPosition3 = 0.28;

	@Override
	public void linear() {
		drive.setPoseEstimate(LeftStart);
		client.putData("初始化点位", ">| _ _");

		final Trajectory suspend_preload = drive.trajectoryBuilder(LeftStart).lineToLinearHeading(LeftSuspend).build();

		final Trajectory get_sample  = drive.trajectoryBuilder(LeftSuspend).lineToLinearHeading(LeftSample).build();
		final Trajectory to_sample_1 = drive.trajectoryBuilder(Decant).lineToLinearHeading(LeftSample.plus(new Pose2d(0, 0, toRadians(- 23)))).build();
		final Trajectory to_sample_2 = drive.trajectoryBuilder(Decant).lineToLinearHeading(LeftSample.plus(new Pose2d(0, 0, toRadians(21.7)))).build();

		final Trajectory decant_1 = drive.trajectoryBuilder(LeftSample).lineToLinearHeading(Decant).build();
		final Trajectory decant_2 = drive.trajectoryBuilder(to_sample_1.end()).lineToLinearHeading(Decant).build();
		final Trajectory decant_3 = drive.trajectoryBuilder(to_sample_2.end()).lineToLinearHeading(Decant).build();

		final TrajectorySequence park = drive.trajectorySequenceBuilder(Decant).lineToLinearHeading(LeftParkPrepare).back(15).build();

		waitForStart();

		utils.integralLiftUpPrepare().liftSuspendHighPrepare().runAsThread();
		drive.followTrajectory(suspend_preload);
		utils.liftSuspendHigh().runCached();
		sleep(500);
		utils.openClip().waitMs(100).liftDown().integralIntakes().scaleOperate(scaleGetPosition1).runAsThread();

		drive.followTrajectory(get_sample);
		utils.displayArms().waitMs(600).integralIntakesEnding().waitMs(1200).openClaw().waitMs(100).closeClaw().waitMs(100).openClaw().waitMs(200).integralLiftUpPrepare().liftDecantHigh().runAsThread();
		sleep(1000);
		drive.followTrajectory(decant_1);

		sleep(1200);
		utils.decant().waitMs(1300).integralLiftDownPrepare().waitMs(500).liftDown().waitMs(500).integralIntakes().rotateRightTurn(0.1).scaleOperate(scaleGetPosition2).runAsThread();
		sleep(1000);
		drive.followTrajectory(to_sample_1);

		sleep(1000);

		utils.displayArms().waitMs(600).integralIntakesEnding().waitMs(1200).openClaw().waitMs(100).closeClaw().waitMs(100).openClaw().waitMs(200).integralLiftUpPrepare().liftDecantHigh().runAsThread();
		sleep(1000);
		drive.followTrajectory(decant_2);

		sleep(1200);
		utils.decant().waitMs(1300).integralLiftDownPrepare().waitMs(500).liftDown().waitMs(500).integralIntakes().rotateRightTurn(- 0.1).displayArms().waitMs(200).scaleOperate(scaleGetPosition3).runAsThread();
		sleep(1500);
		drive.followTrajectory(to_sample_2);

		sleep(1000);

		utils.integralIntakesEnding().waitMs(1200).openClaw().waitMs(100).closeClaw().waitMs(100).openClaw().waitMs(200).integralLiftUpPrepare().liftDecantHigh().runAsThread();
		sleep(1000);
		drive.followTrajectory(decant_3);

		sleep(1200);
		utils.decant().waitMs(1300).integralLiftDownPrepare().waitMs(500).liftDown().liftSuspendLv1().runAsThread();

		sleep(1000);
		drive.followTrajectorySequence(park);
		utils.closeClip().addAction(SimpleDriveOp.build(0, - 0.25, 0)).waitMs(1000).runCached();

		flagging_op_complete();
	}
}
