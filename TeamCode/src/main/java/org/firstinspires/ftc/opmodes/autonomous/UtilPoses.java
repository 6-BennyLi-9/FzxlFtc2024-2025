package org.firstinspires.ftc.opmodes.autonomous;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.jetbrains.annotations.Contract;

public final class UtilPoses {
	public static final Pose2d LeftStart       = p(38, 60, - 90);
	public static final Pose2d Decant          = p(59, 55, - 135);
	public static final Pose2d LeftSample      = p(59, 50, - 90);
	public static final Pose2d LeftParkPrepare = p(36, 10, 0);

	@NonNull
	@Contract("_, _, _ -> new")
	private static Pose2d p(final double x, final double y, final double h) {
		return new Pose2d(x, y, Math.toRadians(h));
	}
}
