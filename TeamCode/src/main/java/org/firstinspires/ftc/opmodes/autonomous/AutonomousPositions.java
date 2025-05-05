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

	/// Left
	@TestSucceed
	public static final Pose2d LEFT_START        = p(40, 65, - 90);
	@TestSucceed
	public static final Pose2d DECANT            = p(55.5, 55.5, - 135);
	/// 夹取样本
	@TestSucceed
	public static final Pose2d LEFT_SAMPLE       = p(59, 49, - 90);
	@TestSucceed
	public static final Pose2d LEFT_PARK_PREPARE = p(36, 10, 0);

	/// Right
	@TestSucceed
	public static final Pose2d RIGHT_START  = p(- 8, 60, 90);
	@TestSucceed
	public static final Pose2d SUSPEND      = p(- 10, 31.5, 90);
	/// 夹取样本
	public static final Pose2d RIGHT_SAMPLE = p(- 49.5, 44.5, - 90);
	public static final Pose2d GET_SUSPEND  = p(- 43, 61, - 90);
	public static final Pose2d RIGHT_PARK   = p(- 45, 60, 90);
}
