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
public enum CoreDatabase {
	;
	@Nullable
	public static Pose2d pose;
	@NonNull
	public static Orientation orientation;
	@NonNull
	public static TerminateReason last_terminateReason;

	public static double autonomous_time_used;

	static {
		pose=null;
		orientation=new Orientation();
		last_terminateReason=TerminateReason.NaturallyShutDown;
		autonomous_time_used=-1;
	}

	public static void writeInVals(@NonNull final IntegralAutonomous autonomous, final TerminateReason terminateReason, final double autonomous_time_used){
		pose=autonomous.drive.getPoseEstimate();
		HardwareDatabase.syncIMU();
		orientation=HardwareDatabase.imu.getAngularOrientation();
		last_terminateReason=terminateReason;
		CoreDatabase.autonomous_time_used=autonomous_time_used;
	}
	public static void writeInVals(@NonNull final IntegralTeleOp tele, final TerminateReason terminateReason){
		pose=null;
		orientation=HardwareDatabase.imu.getAngularOrientation();
		last_terminateReason=terminateReason;
		autonomous_time_used=-1;
	}
}
