package org.exboithpath.path;

import org.betastudio.ftc.time.Timer;
import org.exboithpath.graphics.Pose;

public final class PathMeta {
	private final Path   raw;
	private final double startTimeMills;
	private       boolean isInitialized;

	public PathMeta(Path raw) {
		this(raw, Timer.getCurrentTime());
	}

	public PathMeta(Path raw, double startTimeMills) {
		this.raw = raw;
		this.startTimeMills = startTimeMills;
	}

	public Path getRawPath() {
		return raw;
	}

	public boolean isWithinStarted(){
		return raw instanceof StartWithinPath;
	}

	public Pose getStartingPose() {
		assert raw instanceof StartWithinPath;
		return ((StartWithinPath) raw).getStartPose();
	}

	public Pose update(){
		return raw.getRawDeltaTargetPose(Timer.getCurrentTime() - startTimeMills);
	}

	public boolean isInitialized() {
		return isInitialized;
	}

	public void markInitialDone() {
		isInitialized = true;
	}

	public boolean isTargetFinished() {
		return raw.isPathFinished();
	}
}
