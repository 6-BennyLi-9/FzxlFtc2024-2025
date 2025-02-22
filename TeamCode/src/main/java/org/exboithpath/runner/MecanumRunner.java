package org.exboithpath.runner;

import org.betastudio.ftc.util.entry.Updatable;
import org.exboithpath.graphics.Pose;

public interface MecanumRunner extends Updatable {
	void setPoseTarget(Pose target);
	void shutdown();
}
