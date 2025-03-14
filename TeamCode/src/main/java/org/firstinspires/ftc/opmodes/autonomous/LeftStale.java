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
import org.firstinspires.ftc.teamcode.cores.eventloop.StructuralLinearMode;
import org.firstinspires.ftc.teamcode.cores.structure.SimpleDriveOp;

@Config
@Autonomous(preselectTeleOp = "19419", group = "0_Main")
public class LeftStale extends StructuralLinearMode {
	public static final double scaleGetPosition1 = 0.238;
	public static final double scaleGetPosition2 = 0.2905;
	public static final double scaleGetPosition3 = 0.28;

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

		utils.integralLiftUpPrepare();
		utils.liftSuspendHighPrepare();
		utils.runAsThread();
		drive.followTrajectory(suspend_preload);
		utils.liftSuspendHigh();
		utils.runCached();
		sleep(500);
		utils.openClip();
		utils.waitMs(100);
		utils.liftDown();
		utils.integralIntakes();
		utils.scaleOperate(scaleGetPosition1);
		utils.runAsThread();

		drive.followTrajectory(get_sample);
		utils.displayArms();
		utils.waitMs(600);
		utils.integralIntakesEnding();
		utils.waitMs(1200);
		utils.openClaw().waitMs(100);
		utils.closeClaw().waitMs(100);
		utils.openClaw().waitMs(200);
		utils.integralLiftUpPrepare();
		utils.liftDecantHigh();
		utils.runAsThread();
		sleep(1000);
		drive.followTrajectory(decant_1);

		sleep(1200);
		utils.decant();
		utils.waitMs(1300);
		utils.integralLiftDownPrepare();
		utils.waitMs(500);
		utils.liftDown();
		utils.waitMs(500);
		utils.integralIntakes();
		utils.rotateRightTurn(0.1);
		utils.scaleOperate(scaleGetPosition2);
		utils.runAsThread();
		sleep(1000);
		drive.followTrajectory(to_sample_1);

		sleep(1000);

		utils.displayArms();
		utils.waitMs(600);
		utils.integralIntakesEnding();
		utils.waitMs(1200);
		utils.openClaw();
		utils.waitMs(100);
		utils.closeClaw();
		utils.waitMs(100);
		utils.openClaw();
		utils.waitMs(200);
		utils.integralLiftUpPrepare();
		utils.liftDecantHigh();
		utils.runAsThread();
		sleep(1000);
		drive.followTrajectory(decant_2);

		sleep(1200);
		utils.decant();
		utils.waitMs(1300);
		utils.integralLiftDownPrepare();
		utils.waitMs(500);
		utils.liftDown();
		utils.waitMs(500);
		utils.integralIntakes();
		utils.rotateRightTurn(- 0.1);
		utils.displayArms();
		utils.waitMs(200);
		utils.scaleOperate(scaleGetPosition3);
		utils.runAsThread();
		sleep(1500);
		drive.followTrajectory(to_sample_2);

		sleep(1000);

		utils.integralIntakesEnding();
		utils.waitMs(1200);
		utils.openClaw();
		utils.waitMs(100);
		utils.closeClaw();
		utils.waitMs(100);
		utils.openClaw();
		utils.waitMs(200);
		utils.integralLiftUpPrepare();
		utils.liftDecantHigh();
		utils.runAsThread();
		sleep(1000);
		drive.followTrajectory(decant_3);

		sleep(1200);
		utils.decant();
		utils.waitMs(1300);
		utils.integralLiftDownPrepare();
		utils.waitMs(500);
		utils.liftSuspendLv1();
		utils.runAsThread();

		sleep(1000);
		drive.followTrajectorySequence(park);
		utils.closeClip();
		utils.addAction(SimpleDriveOp.build(0, - 0.25, 0));
		utils.waitMs(1000);
		utils.runCached();

		flagging_op_complete();
	}
}
