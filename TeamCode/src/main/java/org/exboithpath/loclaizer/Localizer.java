package org.exboithpath.loclaizer;

import org.betastudio.ftc.util.entry.ValueUpdatable;
import org.exboithpath.graphics.Pose;

public interface Localizer extends ValueUpdatable<Pose> {
	void setPoseEstimate(Pose newPose);
}
