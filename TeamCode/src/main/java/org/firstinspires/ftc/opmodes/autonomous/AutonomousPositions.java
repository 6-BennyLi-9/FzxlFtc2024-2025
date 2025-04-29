package org.firstinspires.ftc.opmodes.autonomous;

import static org.betastudio.ftc.Annotations.TestSucceed;
import static org.betastudio.ftc.util.Pose2dUtil.p;

import com.acmerobotics.roadrunner.geometry.Pose2d;

/**
 * 自动程序的所有点位！
 * <p>
 * 标记有 {@link TestSucceed} 的不要修改，已测试完毕。
 */
public final class AutonomousPositions {
	@TestSucceed
	public static final Pose2d LeftStart       = p(40, 65, - 90);
	@TestSucceed
	public static final Pose2d Decant          = p(55.5, 55.5, - 135);
	@TestSucceed
	public static final Pose2d LeftSample      = p(59, 49, - 90);
	@TestSucceed
	public static final Pose2d LeftParkPrepare = p(36, 10, 0);

	public static final Pose2d RightStart  = p(- 12, 60, 90);
	public static final Pose2d Suspend     = p(- 10, 32, 90);
	public static final Pose2d RightSample = p(- 49.5, 47.5, - 90);
	public static final Pose2d GetSuspend  = p(- 50, 61, - 90);
}
