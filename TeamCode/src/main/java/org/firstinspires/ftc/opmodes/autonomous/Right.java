package org.firstinspires.ftc.opmodes.autonomous;

import static org.betastudio.ftc.util.Pose2dUtil.xp;
import static org.betastudio.ftc.util.Pose2dUtil.yp;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousPositions.GET_SUSPEND;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousPositions.RIGHT_PARK;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousPositions.RIGHT_SAMPLE;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousPositions.RIGHT_START;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousPositions.SUSPEND;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.betastudio.ftc.action.Actions;
import org.firstinspires.ftc.teamcode.eventloop.integral.ActionBasedAutonomous;

import java.util.ArrayList;
import java.util.List;

@Config
@Autonomous(preselectTeleOp = "19419", group = "1_Beta")
public class Right extends ActionBasedAutonomous {
	public static final int           SUSPEND_COUNT = 2;
	public static final int           INTAKE_COUNT  = 2;
	public static final List <Pose2d> SUSPEND_POSES           = new ArrayList <>();
	public static final List <Pose2d> INTAKE_POSES            = new ArrayList <>();
	public static       double        SCALE_INTAKE_POSITION   = 0.2;
	/// 每一个夹取的样本的距离
	public static       double        INTAKE_SAMPLE_DISTANCE  = - 11;
	/// 到达人类玩家处后前进的距离
	public static       double        GET_SAMPLE_DISTANCE     = 2;
	/// 每一个悬挂的样本的距离
	public static       double        SUSPEND_SAMPLE_DISTANCE = 5;

	static {
		for (int i = 1 ; i <= SUSPEND_COUNT ; i++) {
			SUSPEND_POSES.add(xp(SUSPEND, SUSPEND_SAMPLE_DISTANCE * i));
		}

		for (int i = 0 ; i < INTAKE_COUNT ; i++) {
			INTAKE_POSES.add(xp(RIGHT_SAMPLE, INTAKE_SAMPLE_DISTANCE * i));
		}
	}

	@Override
	public void actionBuildEntry() {
		client.putData("初始化位置", "机器左靠内侧边缘");

		///挂预载
		utils.closeClip();
		Actions.runAction(utils.pack());
		appendSuspend(SUSPEND);

		/// 夹取
		for (int i = 0 ; i < INTAKE_COUNT ; i++) {
			appendIntake(INTAKE_POSES.get(i));
		}

		/// 悬挂
		for (int i = 0 ; i < SUSPEND_COUNT ; i++) {
			appendGetSample(GET_SUSPEND);
			appendSuspend(SUSPEND_POSES.get(i));
		}

		/// 停靠
		executeAssembled(track.runTo(RIGHT_PARK), utils.pack());
	}

	/**
	 * 自动预载电梯下的动作
	 */
	public void appendSuspend(final Pose2d suspend) {
		utils.waitMs(200);
		utils.liftSuspendHighPrepare();
		executeAssembled(track.runTo(suspend), utils.pack());
		utils.liftSuspendHigh();
		utils.openClip();
		utils.waitMs(100);
		executeManager();
		utils.waitMs(200);
		utils.liftDown();
	}

	public void appendGetSample(final Pose2d get) {
		utils.waitMs(100);
		executeAssembled(track.runTo(get), utils.pack());
		utils.waitMs(500);
		utils.closeClip();
		utils.waitMs(500);
		executeLinked(track.runTo(yp(get, GET_SAMPLE_DISTANCE)), utils.pack());
	}

	public void appendIntake(final Pose2d intake) {
		utils.waitMs(200);
		utils.scaleOperate(SCALE_INTAKE_POSITION);
		executeAssembled(track.runTo(intake), utils.pack());
		utils.armDisplay();
		utils.waitMs(600);
		utils.closeClaw();
		utils.waitMs(400);
		executeManager();
		utils.armBack();
		utils.scaleBack();
		utils.waitMs(900);
		utils.openClaw();
		utils.armSafe();
		utils.boxDecant();
		utils.waitMs(500);
		utils.boxRst();
		executeManager();
	}

	@Override
	public Pose2d getInitialPose() {
		return RIGHT_START;
	}
}
