package org.firstinspires.ftc.opmodes.autonomous;

import static org.betastudio.ftc.util.Pose2dUtil.t;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousPositions.DECANT;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousPositions.LEFT_PARK_PREPARE;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousPositions.LEFT_SAMPLE;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousPositions.LEFT_START;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.betastudio.ftc.action.Action;
import org.firstinspires.ftc.teamcode.eventloop.integral.ActionBasedAutonomous;
import org.firstinspires.ftc.teamcode.structure.DriveOp;

import java.util.ArrayList;
import java.util.List;

@Config
@Autonomous(preselectTeleOp = "19419", group = "0_Main")
public class Left extends ActionBasedAutonomous {
	public static final int           SAMPLE_COUNTS        = 3;
	public static final List <Double> SCALE_GET_POSITIONS  = new ArrayList <>();
	public static final List <Pose2d> SAMPLE_POSES         = new ArrayList <>();
	public static       double        SCALE_GET_POSITION_1 = 0.23;
	public static       double        SCALE_GET_POSITION_2 = 0.29;
	public static       double        SCALE_GET_POSITION_3 = 0.29;

	static {
		SCALE_GET_POSITIONS.add(SCALE_GET_POSITION_1);
		SCALE_GET_POSITIONS.add(SCALE_GET_POSITION_2);
		SCALE_GET_POSITIONS.add(SCALE_GET_POSITION_3);

		SAMPLE_POSES.add(LEFT_SAMPLE);
		SAMPLE_POSES.add(t(LEFT_SAMPLE, - 25));
		SAMPLE_POSES.add(t(LEFT_SAMPLE, 25));
	}

	@Override
	public void actionBuildEntry() {
		client.putData("初始化位置", "机器右靠内侧边缘");

		/// 倒预载
		appendDecanting();

		for (int i = 0 ; i < SAMPLE_COUNTS ; i++) {
			/// 夹取
			appendRunningScaling(SCALE_GET_POSITIONS.get(i), SAMPLE_POSES.get(i));
			appendIntake();
			/// 倒出
			appendDecanting();
		}

		/// 停靠
		utils.liftSuspendLv1();
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

	public void appendIntake() {
		utils.armDisplay();
		utils.waitMs(600);
		utils.closeClaw();
		utils.waitMs(250);
		utils.armBack();
		utils.scaleBack();
		utils.waitMs(1050);
		//倒入box
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
