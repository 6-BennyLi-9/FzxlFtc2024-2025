package org.firstinspires.ftc.opmodes.autonomous;

import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.Decant;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.LeftStart;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.LeftParkPrepare;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.LeftSample;
import static java.lang.Math.toRadians;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.utils.LinkedAction;
import org.firstinspires.ftc.teamcode.cores.eventloop.ActionBasedAutonomous;
import org.firstinspires.ftc.teamcode.cores.eventloop.TrajectoryRunnerAction;
import org.firstinspires.ftc.teamcode.cores.structure.SimpleDriveOp;

@Config
@Autonomous(preselectTeleOp = "19419", group = "0_Main")
public class Left extends ActionBasedAutonomous {
	public static final double scaleGetPosition1 = 0.238;
	public static final double scaleGetPosition2 = 0.2905;
	public static final double scaleGetPosition3 = 0.28;

	/// box初始化位置有误
	@Override
	public void actionBuildEntry() {
		appendDecanting();
		appendAfterDecant();

		appendRunningScaling(scaleGetPosition1, LeftSample);
		appendIntake();
		appendDecanting();
		appendAfterDecant();

		appendRunningScaling(scaleGetPosition2, LeftSample.plus(new Pose2d(0, 0, toRadians(- 23))));
		appendIntake();
		appendDecanting();
		appendAfterDecant();

		appendRunningScaling(scaleGetPosition3, LeftSample.plus(new Pose2d(0, 0, toRadians(21.7))));
		appendIntake();
		appendDecanting();
		appendAfterDecant();

		utils.closeClip();
		utils.waitMs(2000);
		utils.addAction(SimpleDriveOp.build(0, - 0.25, 0));

		appendAssembled(
				new TrajectoryRunnerAction(drive, drive.trajectorySequenceBuilder(Decant).lineToLinearHeading(LeftParkPrepare).back(15).build()),
				utils.pack()
		);
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
		appendAssembled(
				liftUpping,
				new LinkedAction(
						track.runTo(Decant),
						decanting
				)
		);
	}

	public void appendAfterDecant(){
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

		appendAssembled(
				utils.pack(),
				track.runTo(pose)
		);
	}
}
