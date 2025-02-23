package org.exboithpath.path;

import org.exboithpath.graphics.Pose;

public class TurnPath implements Path {
	private final double turnTotal;
	private final double DEG_PER_MILLISECOND;
	private       double lastUpdateTime = 0;

	public TurnPath(double turnTotal, double DEG_PER_MILLISECOND) {
		this.turnTotal = turnTotal;
		this.DEG_PER_MILLISECOND = DEG_PER_MILLISECOND;
	}

	@Override
	public boolean isPathFinished() {
		return lastUpdateTime > turnTotal / DEG_PER_MILLISECOND;
	}

	@Override
	public Pose getRawDeltaTargetPose(double time) {
		Pose pose = new Pose(0, 0, (time - lastUpdateTime) * DEG_PER_MILLISECOND);
		lastUpdateTime = time;
		return pose;
	}
}
