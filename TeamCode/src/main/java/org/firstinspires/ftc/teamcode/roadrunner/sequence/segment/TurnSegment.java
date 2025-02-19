package org.firstinspires.ftc.teamcode.roadrunner.sequence.segment;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.profile.MotionProfile;
import com.acmerobotics.roadrunner.trajectory.TrajectoryMarker;
import com.acmerobotics.roadrunner.util.Angle;

import java.util.List;

public final class TurnSegment extends SequenceSegment {
	private final MotionProfile motionProfile;

    public TurnSegment(Pose2d startPose, double totalRotation, @NonNull MotionProfile motionProfile, List<TrajectoryMarker> markers) {
        super(
                motionProfile.duration(),
                startPose,
                new Pose2d(
                        startPose.getX(), startPose.getY(),
                        Angle.norm(startPose.getHeading() + totalRotation)
                ),
                markers
        );

		this.motionProfile = motionProfile;
    }

    public MotionProfile getMotionProfile() {
        return this.motionProfile;
    }
}
