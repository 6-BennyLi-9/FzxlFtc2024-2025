package org.firstinspires.ftc.opmodes.autonomous;

import static org.betastudio.ftc.Annotations.TestSucceed;
import static org.betastudio.ftc.util.Pose2dUtil.p;
import static org.betastudio.ftc.util.Pose2dUtil.t;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;

/**
 * 自动程序的所有参数数据！
 * <p>
 * 标记有 {@link TestSucceed} 的不要修改，已测试完毕。
 */
@Config
public final class AutonomousConfigures {
	/* Left  点位 */
	@TestSucceed
	public static final Pose2d LEFT_START        = p(40, 65, - 90);
	@TestSucceed
	public static final Pose2d DECANT            = p(55.5, 55.5, - 135);
	@TestSucceed
	public static final Pose2d LEFT_SAMPLE_1     = p(58, 49, - 90);
	@TestSucceed
	public static final Pose2d LEFT_SAMPLE_2     = t(LEFT_SAMPLE_1, - 23);
	@TestSucceed
	public static final Pose2d LEFT_SAMPLE_3     = t(LEFT_SAMPLE_1, 25);
	@TestSucceed
	public static final Pose2d LEFT_PARK_PREPARE = p(38, 10, 180);
	/* Right 点位 */
	@TestSucceed
	public static final Pose2d RIGHT_START       = p(- 8, 60, 90);
	@TestSucceed
	public static final Pose2d SUSPEND_1         = p(- 11.5, 32, 90);
	public static final Pose2d SUSPEND_2         = p(- 2, 32, 90);
	public static final Pose2d SUSPEND_3         = p(6, 32, 90);
	public static final Pose2d RIGHT_SAMPLE_1    = p(- 50, 44, - 90);
	public static final Pose2d RIGHT_SAMPLE_2    = p(- 60.5, 44, - 90);
	public static final Pose2d GET_SUSPEND       = p(- 43, 60, - 90);
	public static final Pose2d RIGHT_PARK        = p(- 45, 60, 90);

	/* Left  硬件参数 */
	public static double LEFT_SCALE_INTAKE_POSITION_1  = 0.32;
	public static double LEFT_SCALE_INTAKE_POSITION_2  = 0.34;
	public static double LEFT_SCALE_INTAKE_POSITION_3  = 0.36;
	/* Right 硬件参数 */
	public static double RIGHT_SCALE_INTAKE_POSITION   = 0.3;
	/// 到达人类玩家处后前进的距离
	public static double RIGHT_GET_SUSPENDING_DISTANCE = 1.5;
}
