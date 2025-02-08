package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.cores.eventloop.IntegralLinearMode;
import org.firstinspires.ftc.teamcode.cores.eventloop.IntegralTeleOp;
import org.firstinspires.ftc.teamcode.cores.eventloop.TerminateReason;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * CoreDatabase 类用于存储机器人在不同操作模式下的状态信息。
 * 包含机器人的位置 (Pose2d)、方向 (Orientation)、上次终止的原因 (TerminateReason) 以及自动模式下使用的时间。
 */
@Config
public final class CoreDatabase {
	/**
	 * 机器人的位置，可以为空，表示位置未被估计或记录。
	 */
	@Nullable
	public static Pose2d pose;

	/**
	 * 机器人的方向，不能为空。
	 */
	@NonNull
	public static Orientation orientation;

	/**
	 * 上次操作模式终止的原因，默认为自然关闭。
	 */
	@NonNull
	public static TerminateReason last_terminateReason;

	/**
	 * 自动模式下使用的时间，单位为秒。
	 */
	public static double autonomous_time_used;

	/**
	 * 标记上次运行的操作模式是否为自动模式。
	 */
	public static boolean last_is_autonomous;

	static {
		pose = null;
		orientation = new Orientation();
		last_terminateReason = TerminateReason.NATURALLY_SHUT_DOWN;
		autonomous_time_used = - 1;
	}

	/**
	 * 此方法用于在自动模式下记录机器人的位置、方向、终止原因以及使用的时间。
	 *
	 * @param autonomous           正在运行的自动模式操作实例，不能为 null。
	 * @param terminateReason      操作模式终止的原因。
	 * @param autonomous_time_used 自动模式下使用的时间，单位为秒。
	 */
	public static void writeInVals(@NonNull final IntegralLinearMode autonomous, final TerminateReason terminateReason, final double autonomous_time_used) {
		pose = autonomous.drive.getPoseEstimate();
		//		HardwareDatabase.syncIMU();
		orientation = HardwareDatabase.imu.getAngularOrientation();
		last_is_autonomous = true;
		last_terminateReason = terminateReason;
		CoreDatabase.autonomous_time_used = autonomous_time_used;
	}

	/**
	 * 此方法用于在遥控操作模式下记录机器人的方向和终止原因。
	 *
	 * @param tele            正在运行的遥控操作模式实例，不能为 null。
	 * @param terminateReason 操作模式终止的原因。
	 */
	public static void writeInVals(@NonNull final IntegralTeleOp tele, final TerminateReason terminateReason) {
		pose = null;
		orientation = HardwareDatabase.imu.getAngularOrientation();
		last_terminateReason = terminateReason;
		last_is_autonomous = false;
	}
}
