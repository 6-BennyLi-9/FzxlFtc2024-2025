package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.cores.eventloop.IntegralAutonomous;
import org.firstinspires.ftc.cores.eventloop.IntegralTeleOp;
import org.firstinspires.ftc.cores.eventloop.TerminateReason;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Config
public final class CoreDatabase {
	@Nullable
	public static Pose2d          pose;
	@NonNull
	public static Orientation     orientation;
	@NonNull
	public static TerminateReason last_terminateReason;

	public static double autonomous_time_used;

	static {
		pose = null;
		orientation = new Orientation();
		last_terminateReason = TerminateReason.NATURALLY_SHUT_DOWN;
		autonomous_time_used = - 1;
	}

	public static void writeInVals(@NonNull IntegralAutonomous autonomous, TerminateReason terminateReason, double autonomous_time_used) {
		pose = autonomous.drive.getPoseEstimate();
		HardwareDatabase.syncIMU();
		orientation = HardwareDatabase.imu.getAngularOrientation();
		CoreDatabase.last_terminateReason = terminateReason;
		CoreDatabase.autonomous_time_used = autonomous_time_used;
	}

	public static void writeInVals(@NonNull IntegralTeleOp tele, TerminateReason terminateReason) {
		pose = null;
		orientation = HardwareDatabase.imu.getAngularOrientation();
		CoreDatabase.last_terminateReason = terminateReason;
		CoreDatabase.autonomous_time_used = - 1;
	}
}
