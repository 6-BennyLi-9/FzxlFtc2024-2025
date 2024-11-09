package org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.sequencesegment.SequenceSegment;

import java.util.Collections;
import java.util.List;

public class TrajectorySequence {
    private final List<SequenceSegment> sequenceList;

    public TrajectorySequence(@NonNull final List<SequenceSegment> sequenceList) {
        if (sequenceList.isEmpty()) throw new EmptySequenceException();

        this.sequenceList = Collections.unmodifiableList(sequenceList);
    }

    public Pose2d start() {
        return sequenceList.get(0).getStartPose();
    }

    public Pose2d end() {
        return sequenceList.get(sequenceList.size() - 1).getEndPose();
    }

    public double duration() {
        double total = 0.0;

        for (final SequenceSegment segment : sequenceList) {
            total += segment.getDuration();
        }

        return total;
    }

    public SequenceSegment get(final int i) {
        return sequenceList.get(i);
    }

    public int size() {
        return sequenceList.size();
    }
}
