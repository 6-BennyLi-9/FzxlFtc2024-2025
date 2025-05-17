package org.firstinspires.ftc.opmodes.autonomous;

import static org.betastudio.ftc.util.Pose2dUtil.yp;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousConfigures.RIGHT_START;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousConfigures.GET_SUSPEND;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousConfigures.RIGHT_PARK;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousConfigures.RIGHT_SAMPLE_1;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousConfigures.RIGHT_SAMPLE_2;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousConfigures.SUSPEND_1;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousConfigures.SUSPEND_2;
import static org.firstinspires.ftc.opmodes.autonomous.AutonomousConfigures.SUSPEND_3;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.betastudio.ftc.action.Actions;
import org.firstinspires.ftc.teamcode.eventloop.integral.ActionBasedAutonomous;

@Config
@Autonomous(preselectTeleOp = "19419", group = "1_Beta")
public class Right extends ActionBasedAutonomous {

	@Override
	public void actionBuildEntry() {
		client.putData("初始化位置", "机器左靠内侧边缘");

		/// 挂预载
		utils.closeClip();
		Actions.runAction(utils.pack());
		appendSuspend(SUSPEND_1);

		/// 夹取
		appendIntake(RIGHT_SAMPLE_1);
		appendIntake(RIGHT_SAMPLE_2);

		/// 悬挂
		appendGetSample(GET_SUSPEND);
		appendSuspend(SUSPEND_2);
		appendGetSample(GET_SUSPEND);
		appendSuspend(SUSPEND_3);

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
		utils.waitMs(300);
		utils.closeClip();
		utils.waitMs(400);
		executeLinked(track.runTo(yp(get, AutonomousConfigures.GET_SUSPENDING_SAMPLE_DISTANCE)), utils.pack());
	}

	public void appendIntake(final Pose2d intake) {
		utils.waitMs(150);
		utils.scaleOperate(AutonomousConfigures.RIGHT_SCALE_INTAKE_POSITION);
		executeAssembled(track.runTo(intake), utils.pack());
		utils.armDisplay();
		utils.waitMs(600);
		utils.closeClaw();
		utils.waitMs(330);
		executeManager();
		utils.armBack();
		utils.scaleBack();
		utils.waitMs(900);
		utils.openClaw();
		utils.armSafe();
		utils.waitMs(100);
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
