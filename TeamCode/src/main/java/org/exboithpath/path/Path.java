package org.exboithpath.path;

import org.exboithpath.graphics.Pose;

public interface Path {
	boolean isPathFinished();

	/**
	 * @param time 单位：毫秒
	 */
	Pose getRawDeltaTargetPose(double time);
}
