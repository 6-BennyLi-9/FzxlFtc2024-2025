package org.acmerobotics.roadrunner.trajectorysequence.sequencesegment;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.TrajectoryMarker;

import java.util.List;

public final class WaitSegment extends SequenceSegment {
	public WaitSegment(final Pose2d pose, final double seconds, final List <TrajectoryMarker> markers) {
		super(seconds, pose, pose, markers);
	}
}
