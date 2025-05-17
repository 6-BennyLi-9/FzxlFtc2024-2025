package org.firstinspires.ftc.opmodes.autonomous;

import static org.firstinspires.ftc.opmodes.autonomous.AutonomousConfigures.DECANT;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousConfigures.LEFT_PARK_PREPARE;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousConfigures.LEFT_SAMPLE_1;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousConfigures.LEFT_SAMPLE_2;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousConfigures.LEFT_SAMPLE_3;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousConfigures.LEFT_SCALE_INTAKE_POSITION_1;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousConfigures.LEFT_SCALE_INTAKE_POSITION_2;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousConfigures.LEFT_SCALE_INTAKE_POSITION_3;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousConfigures.LEFT_START;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.eventloop.integral.ActionBasedAutonomous;
import org.firstinspires.ftc.teamcode.structure.DriveOp;

@Autonomous(preselectTeleOp = "19419", group = "0_Main")
public class Left extends ActionBasedAutonomous {
	@Override
	public void actionBuildEntry() {
		client.putData("初始化位置", "机器右靠内侧边缘");

		/// 倒预载
		appendDecanting();

		/// 夹取 1
		appendIntake(LEFT_SCALE_INTAKE_POSITION_1, LEFT_SAMPLE_1);
		/// 倒出
		appendDecanting();
		/// 夹取 2
		appendIntake(LEFT_SCALE_INTAKE_POSITION_2, LEFT_SAMPLE_2);
		/// 倒出
		appendDecanting();
		/// 夹取 3
		appendIntake(LEFT_SCALE_INTAKE_POSITION_3, LEFT_SAMPLE_3);
		/// 倒出
		appendDecanting();

		/// 停靠
		utils.liftDown();
		utils.closeClip();

		executeLinked(track.runTo(LEFT_PARK_PREPARE));
		executeAssembled(utils.pack(), DriveOp.build(0, 0.25, 0));
	}

	@Override
	public Pose2d getInitialPose() {
		return LEFT_START;
	}

	public void appendDecanting() {
		utils.armSafe();
		utils.liftDecantHigh();
		final Action liftUpping = utils.pack();
		utils.waitMs(50);
		utils.boxDecant();
		utils.waitMs(700);
		utils.boxRst();
		final Action decanting = utils.pack();
		executeLinked(liftUpping, track.runTo(DECANT), decanting);
	}

	public void appendIntake(final double scalePose, final Pose2d pose) {
		utils.waitMs(750);
		utils.liftDown();
		utils.scaleOperate(scalePose);

		executeAssembled(utils.pack(), track.runTo(pose));

		utils.armDisplay();
		utils.waitMs(600);
		utils.closeClaw();
		utils.waitMs(250);
		utils.armBack();
		utils.scaleBack();
		utils.waitMs(1050);
		/// 倒入box
		utils.halfOpenClaw();
		utils.waitMs(100);
		utils.openClaw();
		utils.waitMs(50);
		executeManager();
	}
}
