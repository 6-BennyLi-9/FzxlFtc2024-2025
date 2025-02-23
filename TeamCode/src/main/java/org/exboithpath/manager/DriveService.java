package org.exboithpath.manager;

import org.betastudio.ftc.util.entry.RawUpdatable;
import org.exboithpath.graphics.Pose;
import org.exboithpath.path.Path;
import org.exboithpath.runner.RunnerStatus;

public interface DriveService extends RawUpdatable {
	void runTillRunnerStatus(RunnerStatus status);

	@Override
	void update();

	void setupPathMeta(Path path, RunnerStatus targetWait);

	void runToPose(Pose target);

	void turn(double headingRad);

	void lineTo(Pose to);

	void lineTo(Pose from, Pose to);

	void setPoseEst(Pose localizePose);
	Pose getPoseEst();

	RunnerStatus getTargetIDLEStatus();

	void setTargetIDLEStatus(RunnerStatus targetIDLEStatus);

	RunnerStatus getRunnerStatus();
}
