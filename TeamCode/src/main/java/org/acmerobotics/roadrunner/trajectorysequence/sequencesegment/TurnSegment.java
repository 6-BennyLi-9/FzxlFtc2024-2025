package org.acmerobotics.roadrunner.trajectorysequence.sequencesegment;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.profile.MotionProfile;
import com.acmerobotics.roadrunner.trajectory.TrajectoryMarker;
import com.acmerobotics.roadrunner.util.Angle;

import java.util.List;

public final class TurnSegment extends SequenceSegment {
	private final double        totalRotation;
	private final MotionProfile motionProfile;

	public TurnSegment(final Pose2d startPose, final double totalRotation, final MotionProfile motionProfile, final List <TrajectoryMarker> markers) {
		super(motionProfile.duration(), startPose, new Pose2d(startPose.getX(), startPose.getY(), Angle.norm(startPose.getHeading() + totalRotation)), markers);

		this.totalRotation = totalRotation;
		this.motionProfile = motionProfile;
	}

	public double getTotalRotation() {
		return this.totalRotation;
	}

	public MotionProfile getMotionProfile() {
		return this.motionProfile;
	}
}
