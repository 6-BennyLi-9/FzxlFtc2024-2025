package org.exboithpath.runner;

import org.exboithpath.graphics.Pose;
import org.exboithpath.loclaizer.Localizer;

public interface MecanumRunner {
	void setPoseTarget(Pose target);
	void shutdown();
	void start();
	void syncPose(Localizer localizer);
	void update(Localizer localizer);

	RunnerStatus getStatus();

	Pose getPoseTarget();
}
