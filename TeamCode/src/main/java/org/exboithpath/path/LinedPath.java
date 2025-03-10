package org.exboithpath.path;

import androidx.annotation.NonNull;

import org.exboithpath.graphics.Pose;

public class LinedPath implements StartWithinPath{
	private final Pose begin;
	private final Pose end;
	private final double INCH_PER_MILLISECOND;
	private final double X_INCH_PER_MILLISECOND;
	private final double Y_INCH_PER_MILLISECOND;
	private final double DEG_PER_MILLISECOND;
	private       double lastUpdateTime = 0;

	public LinedPath(@NonNull Pose begin, Pose end, double INCH_PER_MILLISECOND, double DEG_PER_MILLISECOND) {
		this.begin = begin;
		this.end = end;
		this.INCH_PER_MILLISECOND = INCH_PER_MILLISECOND;
		this.DEG_PER_MILLISECOND = DEG_PER_MILLISECOND;

		double time = begin.disTo(end) / INCH_PER_MILLISECOND;
		Pose   track = begin.poseTrackTo(end);
		X_INCH_PER_MILLISECOND = track.x / time;
		Y_INCH_PER_MILLISECOND = track.y / time;
	}

	@Override
	public Pose getStartPose() {
		return begin;
	}

	@Override
	public boolean isPathFinished() {
		return lastUpdateTime > begin.disTo(end) / INCH_PER_MILLISECOND && lastUpdateTime > (end.heading-begin.heading) / DEG_PER_MILLISECOND;
	}

	@Override
	public Pose getRawDeltaTargetPose(double time) {
		Pose pose = new Pose((time - lastUpdateTime) * X_INCH_PER_MILLISECOND, (time - lastUpdateTime) * Y_INCH_PER_MILLISECOND, (time - lastUpdateTime) * DEG_PER_MILLISECOND);
		lastUpdateTime = time;
		return pose;
	}
}
