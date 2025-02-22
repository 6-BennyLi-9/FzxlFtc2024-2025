package org.exboithpath.runner;

import org.betastudio.ftc.util.entry.Updatable;
import org.exboithpath.graphics.Pose;
import org.exboithpath.loclaizer.Localizer;

public interface MecanumRunner extends Updatable {
	void setPoseTarget(Pose target);
	void shutdown();
	void syncPose(Localizer localizer);
}
