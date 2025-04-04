package org.firstinspires.ftc.opmodes.autonomous;

import com.acmerobotics.dashboard.config.*;
import com.acmerobotics.roadrunner.geometry.*;
import com.qualcomm.robotcore.eventloop.opmode.*;
import org.betastudio.ftc.action.*;
import org.betastudio.ftc.action.utils.*;
import org.firstinspires.ftc.teamcode.cores.eventloop.*;

import static java.lang.Math.toRadians;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.*;

@Config
@Autonomous(preselectTeleOp = "19419", group = "0_Main")
public class Left extends ActionBasedAutonomous {
	public static final double scaleGetPosition1 = 0.238;
	public static final double scaleGetPosition2 = 0.2905;
	public static final double scaleGetPosition3 = 0.28;

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

		builder.append(new TrajectoryRunnerAction(drive, drive.trajectorySequenceBuilder(Decant).lineToLinearHeading(LeftParkPrepare).back(15).build()));
	}

	@Override
	public Pose2d getInitialPose() {
		return LeftDecantingStart;
	}

	public void appendDecanting() {
		utils.armSafe();
		utils.liftDecantHigh();
		final Action liftUpping = utils.pack();
		utils.boxDecant();
		final Action decanting = utils.pack();
		appendThreaded(
				liftUpping,
				new LinkedAction(
						track.runTo(Decant),
						decanting
				)
		);
	}

	public void appendAfterDecant(){
		utils.waitMs(200);
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

		appendThreaded(
				utils.pack(),
				track.runTo(pose)
		);
	}
}
