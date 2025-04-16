package org.firstinspires.ftc.opmodes.autonomous;

import static org.firstinspires.ftc.opmodes.autonomous.AutonomousPositions.Decant;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousPositions.LeftParkPrepare;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousPositions.LeftSample;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousPositions.LeftStart;
import static java.lang.Math.toRadians;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.utils.LinkedAction;
import org.firstinspires.ftc.teamcode.eventloop.integral.ActionBasedAutonomous;
import org.firstinspires.ftc.teamcode.eventloop.TrajectoryRunnerAction;
import org.firstinspires.ftc.teamcode.structure.DriveOp;

import java.util.ArrayList;
import java.util.List;

@Config
@Autonomous(preselectTeleOp = "19419", group = "0_Main")
public class Left extends ActionBasedAutonomous {
	public static final double SCALE_GET_POSITION_1 = 0.238;
	public static final double SCALE_GET_POSITION_2 = 0.2905;
	public static final double SCALE_GET_POSITION_3 = 0.28;
	public static final int    LOOP_TIME            = 3;

	@Override
	public void actionBuildEntry() {
		appendDecanting();
		appendAfterDecant();

		List <Double> scale_get    = new ArrayList <>();
		List <Pose2d> sample_poses = new ArrayList <>();

		scale_get.add(SCALE_GET_POSITION_1);
		scale_get.add(SCALE_GET_POSITION_2);
		scale_get.add(SCALE_GET_POSITION_3);

		sample_poses.add(LeftSample);
		sample_poses.add(LeftSample.plus(new Pose2d(0, 0, toRadians(- 23))));
		sample_poses.add(LeftSample.plus(new Pose2d(0, 0, toRadians(21.7))));

		for (int i = 0 ; i < LOOP_TIME ; i++) {
			appendRunningScaling(scale_get.get(i), sample_poses.get(i));
			appendIntake();
			appendDecanting();
			appendAfterDecant();
		}

		utils.closeClip();
		utils.waitMs(2000);
		utils.addAction(DriveOp.build(0, - 0.25, 0));

		appendAssembled(new TrajectoryRunnerAction(drive, drive.trajectorySequenceBuilder(Decant).lineToLinearHeading(LeftParkPrepare).back(15).build()), utils.pack());
	}

	@Override
	public Pose2d getInitialPose() {
		return LeftStart;
	}

	public void appendDecanting() {
		utils.armSafe();
		utils.liftDecantHigh();
		final Action liftUpping = utils.pack();
		utils.boxDecant();
		final Action decanting = utils.pack();
		appendAssembled(liftUpping, new LinkedAction(track.runTo(Decant), decanting));
	}

	public void appendAfterDecant() {
		utils.waitMs(300);
		utils.boxRst();
		inputMngAction();
	}

	public void appendIntake() {
		utils.armDisplay();
		utils.waitMs(600);
		utils.closeClaw();
		utils.waitMs(250);
		utils.armBack();
		utils.scaleBack();
		utils.waitMs(1200);
		utils.openClaw();
		utils.waitMs(100);
		utils.closeClaw();
		utils.waitMs(100);
		utils.openClaw();
		inputMngAction();
	}

	public void appendRunningScaling(final double scalePose, final Pose2d pose) {
		utils.liftDown();
		utils.scaleOperate(scalePose);

		appendAssembled(utils.pack(), track.runTo(pose));
	}
}
