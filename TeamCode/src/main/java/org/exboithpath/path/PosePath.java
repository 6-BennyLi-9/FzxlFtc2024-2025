package org.exboithpath.path;

import org.exboithpath.graphics.Pose;

public class PosePath implements StartWithinPath {
	private final Pose pose;

	public PosePath(Pose pose) {
		this.pose = pose;
	}

	@Override
	public boolean isPathFinished() {
		return true;
	}

	@Override
	public Pose getRawDeltaTargetPose(double time) {
		return new Pose(0,0,0);
	}

	@Override
	public Pose getStartPose() {
		return pose;
	}
}
