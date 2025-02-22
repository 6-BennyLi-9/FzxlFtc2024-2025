package org.exboithpath.path;

import org.exboithpath.graphics.Pose;

public interface Path {
	boolean isPathFinished();

	/**
	 * @param time 单位：秒
	 */
	Pose getRawDeltaTargetPose(double time);
}
