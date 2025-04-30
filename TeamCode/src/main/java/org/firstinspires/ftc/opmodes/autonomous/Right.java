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
	public static final double        GET_SAMPLE_DISTANCE     = 2;
	public static final double        INTAKE_SAMPLE_DISTANCE  = - 5;
	public static final double        SUSPEND_SAMPLE_DISTANCE = 5;
	public static final int           SUSPEND_COUNT           = 2;
	public static final int           INTAKE_COUNT            = 2;
	public static final List <Pose2d> SUSPEND_POSES           = new ArrayList <>();
	public static final List <Pose2d> INTAKE_POSES            = new ArrayList <>();

	static {
		for (int i = 0 ; i < SUSPEND_COUNT ; i++) {
			SUSPEND_POSES.add(xp(SUSPEND, SUSPEND_SAMPLE_DISTANCE * (i + 1)));
		}

		for (int i = 0 ; i < INTAKE_COUNT ; i++) {
			INTAKE_POSES.add(xp(RIGHT_SAMPLE, INTAKE_SAMPLE_DISTANCE * i));
		}
	}

	@Override
	public void actionBuildEntry() {
		utils.closeClip();
		Actions.runAction(utils.pack());
		appendSuspend(SUSPEND);

		for (int i = 0 ; i < SUSPEND_COUNT ; i++) {
			appendGetSample(GET_SUSPEND);
			appendSuspend(SUSPEND_POSES.get(i));
		}

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
		executeManager();
		utils.waitMs(200);
		utils.liftDown();
	}

	public void appendGetSample(final Pose2d get) {
		executeAssembled(track.runTo(get), utils.pack());
		utils.waitMs(500);
		utils.closeClip();
		utils.waitMs(500);
		executeLinked(track.runTo(yp(get, GET_SAMPLE_DISTANCE)), utils.pack());
	}

	@Override
	public Pose2d getInitialPose() {
		return RIGHT_START;
	}
}
