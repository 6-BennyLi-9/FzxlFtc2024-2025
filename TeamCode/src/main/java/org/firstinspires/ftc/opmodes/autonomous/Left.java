package org.firstinspires.ftc.opmodes.autonomous;

import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.Decant;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.LeftDecantingStart;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.LeftParkPrepare;
import static org.firstinspires.ftc.opmodes.autonomous.UtilPoses.LeftSample;

import static java.lang.Math.toRadians;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.betastudio.ftc.action.utils.LinkedAction;
import org.firstinspires.ftc.teamcode.cores.eventloop.ActionBasedAutonomous;
import org.firstinspires.ftc.teamcode.cores.eventloop.TrajectoryRunnerAction;

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
		appendThreaded(
				utils.armSafe().liftDecantHigh().pack(),
				new LinkedAction(
						track.runTo(Decant),
						utils.boxDecant().pack()
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

	public void appendRunningScaling(double scalePose, Pose2d pose) {
		appendThreaded(
				utils.liftDown().scaleOperate(scalePose).pack(),
				track.runTo(pose)
		);
	}
}
