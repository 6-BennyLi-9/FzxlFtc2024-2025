package org.betastudio.ftc.util;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.jetbrains.annotations.Contract;

public final class Pose2dUtil {
	@NonNull
	@Contract("_, _, _ -> new")
	public static Pose2d p(final double x, final double y, final double h) {
		return new Pose2d(x, y, Math.toRadians(h));
	}

	@NonNull
	@Contract("_, _ -> new")
	public static Pose2d t(@NonNull final Pose2d p, final double h) {
		return p.plus(new Pose2d(0, 0, Math.toRadians(h)));
	}

	@NonNull
	@Contract("_, _ -> new")
	public static Pose2d xp(@NonNull final Pose2d p, final double x) {
		return p.plus(new Pose2d(x, 0, 0));
	}

	@NonNull
	@Contract("_, _ -> new")
	public static Pose2d yp(@NonNull final Pose2d p, final double y) {
		return p.plus(new Pose2d(0, y, 0));
	}
}
