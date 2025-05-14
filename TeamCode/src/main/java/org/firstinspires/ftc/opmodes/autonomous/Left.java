package org.firstinspires.ftc.opmodes.autonomous;

import static org.betastudio.ftc.util.Pose2dUtil.t;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousConfigures.DECANT;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousConfigures.LEFT_PARK_PREPARE;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousConfigures.LEFT_SAMPLE;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousConfigures.LEFT_START;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.eventloop.integral.ActionBasedAutonomous;
import org.firstinspires.ftc.teamcode.structure.DriveOp;

@Config
@Autonomous(preselectTeleOp = "19419", group = "0_Main")
public class Left extends ActionBasedAutonomous {
	public static       double        SCALE_GET_POSITION_1 = 0.23;
	public static       double        SCALE_GET_POSITION_2 = 0.29;
	public static       double        SCALE_GET_POSITION_3 = 0.29;
	@Override
	public void actionBuildEntry() {
		client.putData("初始化位置", "机器右靠内侧边缘");

		/// 倒预载
		appendDecanting();

		/// 夹取 1
		appendRunningScaling(SCALE_GET_POSITION_1, LEFT_SAMPLE);
		appendIntake();
		/// 倒出
		appendDecanting();
		/// 夹取 2
		appendRunningScaling(SCALE_GET_POSITION_2, t(LEFT_SAMPLE, - 25));
		appendIntake();
		/// 倒出
		appendDecanting();
		/// 夹取 3
		appendRunningScaling(SCALE_GET_POSITION_3, t(LEFT_SAMPLE, 25));
		appendIntake();
		/// 倒出
		appendDecanting();

		/// 停靠
		utils.liftDown();
		utils.closeClip();

		executeLinked(track.runTo(LEFT_PARK_PREPARE));
		executeAssembled(utils.pack(), DriveOp.build(0, - 0.25, 0));
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

	/// 夹取样本
	public void appendIntake() {
		utils.armDisplay();
		utils.waitMs(600);
		utils.closeClaw();
		utils.waitMs(250);
		utils.armBack();
		utils.scaleBack();
		utils.waitMs(1050);
		/// 倒入box
		utils.openClaw();
		utils.waitMs(50);
		utils.closeClaw();
		utils.waitMs(50);
		utils.openClaw();
		utils.waitMs(50);
		executeManager();
	}

	public void appendRunningScaling(final double scalePose, final Pose2d pose) {
		utils.waitMs(750);
		utils.liftDown();
		utils.scaleOperate(scalePose);

		executeAssembled(utils.pack(), track.runTo(pose));
	}
}
