package org.firstinspires.ftc.opmodes.autonomous;

import static org.betastudio.ftc.Annotations.TestSucceed;
import static org.betastudio.ftc.util.Pose2dUtil.p;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;

/**
 * 自动程序的所有参数数据！
 * <p>
 * 标记有 {@link TestSucceed} 的不要修改，已测试完毕。
 */
@Config
public final class AutonomousConfigures {
	/// Left点位
	@TestSucceed
	public static Pose2d LEFT_START                   = p(40, 65, - 90);
	@TestSucceed
	public static Pose2d DECANT                       = p(55.5, 55.5, - 135);
	/// 夹取样本
	@TestSucceed
	public static Pose2d LEFT_SAMPLE                  = p(58, 49, - 90);
	@TestSucceed
	public static Pose2d LEFT_PARK_PREPARE            = p(38, 10, 180);
	/// Left硬件参数
	@TestSucceed
	public static double LEFT_SCALE_INTAKE_POSITION_1 = 0.23;
	@TestSucceed
	public static double LEFT_SCALE_INTAKE_POSITION_2 = 0.29;
	@TestSucceed
	public static double LEFT_SCALE_INTAKE_POSITION_3 = 0.29;

	/// Right点位
	@TestSucceed
	public static Pose2d RIGHT_START                    = p(- 8, 60, 90);
	@TestSucceed
	public static Pose2d SUSPEND_1                      = p(- 11.5, 32, 90);
	public static Pose2d SUSPEND_2                      = p(- 2, 32, 90);
	public static Pose2d SUSPEND_3                      = p(6, 32, 90);
	/// 夹取样本
	public static Pose2d RIGHT_SAMPLE_1                 = p(- 50, 44, - 90);
	public static Pose2d RIGHT_SAMPLE_2                 = p(- 60.5, 44, - 90);
	public static Pose2d GET_SUSPEND                    = p(- 43, 60, - 90);
	public static Pose2d RIGHT_PARK                     = p(- 45, 60, 90);
	/**
	 * 右边硬件参数<P>
	 * 夹取样本时滑轨伸出距离
	 */
	public static double RIGHT_SCALE_INTAKE_POSITION    = 0.21;
	/// 到达人类玩家处后前进的距离
	public static double GET_SUSPENDING_SAMPLE_DISTANCE = 1.5;
}
