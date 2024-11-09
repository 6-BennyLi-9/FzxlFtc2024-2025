package org.firstinspires.ftc.teamcode.trajectorysequence;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.path.PathContinuityViolationException;
import com.acmerobotics.roadrunner.profile.MotionProfile;
import com.acmerobotics.roadrunner.profile.MotionProfileGenerator;
import com.acmerobotics.roadrunner.profile.MotionState;
import com.acmerobotics.roadrunner.trajectory.DisplacementMarker;
import com.acmerobotics.roadrunner.trajectory.DisplacementProducer;
import com.acmerobotics.roadrunner.trajectory.MarkerCallback;
import com.acmerobotics.roadrunner.trajectory.SpatialMarker;
import com.acmerobotics.roadrunner.trajectory.TemporalMarker;
import com.acmerobotics.roadrunner.trajectory.TimeProducer;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.TrajectoryMarker;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.acmerobotics.roadrunner.util.Angle;

import org.firstinspires.ftc.teamcode.trajectorysequence.sequencesegment.SequenceSegment;
import org.firstinspires.ftc.teamcode.trajectorysequence.sequencesegment.TrajectorySegment;
import org.firstinspires.ftc.teamcode.trajectorysequence.sequencesegment.TurnSegment;
import org.firstinspires.ftc.teamcode.trajectorysequence.sequencesegment.WaitSegment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TrajectorySequenceBuilder {
    private final double resolution = 0.25;

    private final TrajectoryVelocityConstraint baseVelConstraint;
    private final TrajectoryAccelerationConstraint baseAccelConstraint;

    private TrajectoryVelocityConstraint currentVelConstraint;
    private TrajectoryAccelerationConstraint currentAccelConstraint;

    private final double baseTurnConstraintMaxAngVel;
    private final double baseTurnConstraintMaxAngAccel;

    private double currentTurnConstraintMaxAngVel;
    private double currentTurnConstraintMaxAngAccel;

    private final List<SequenceSegment> sequenceSegments;

    private final List<TemporalMarker> temporalMarkers;
    private final List<DisplacementMarker> displacementMarkers;
    private final List<SpatialMarker> spatialMarkers;

    private Pose2d lastPose;

    private double tangentOffset;

    private boolean setAbsoluteTangent;
    private double absoluteTangent;

    private TrajectoryBuilder currentTrajectoryBuilder;

    private double currentDuration;
    private double currentDisplacement;

    private double lastDurationTraj;
    private double lastDisplacementTraj;

    public TrajectorySequenceBuilder(
		    final Pose2d startPose,
		    final Double startTangent,
		    final TrajectoryVelocityConstraint baseVelConstraint,
		    final TrajectoryAccelerationConstraint baseAccelConstraint,
		    final double baseTurnConstraintMaxAngVel,
		    final double baseTurnConstraintMaxAngAccel
    ) {
        this.baseVelConstraint = baseVelConstraint;
        this.baseAccelConstraint = baseAccelConstraint;

        this.currentVelConstraint = baseVelConstraint;
        this.currentAccelConstraint = baseAccelConstraint;

        this.baseTurnConstraintMaxAngVel = baseTurnConstraintMaxAngVel;
        this.baseTurnConstraintMaxAngAccel = baseTurnConstraintMaxAngAccel;

        this.currentTurnConstraintMaxAngVel = baseTurnConstraintMaxAngVel;
        this.currentTurnConstraintMaxAngAccel = baseTurnConstraintMaxAngAccel;

        sequenceSegments = new ArrayList<>();

        temporalMarkers = new ArrayList<>();
        displacementMarkers = new ArrayList<>();
        spatialMarkers = new ArrayList<>();

        lastPose = startPose;

        tangentOffset = 0.0;

        setAbsoluteTangent = (null != startTangent);
        absoluteTangent = null != startTangent ? startTangent : 0.0;

        currentTrajectoryBuilder = null;

        currentDuration = 0.0;
        currentDisplacement = 0.0;

        lastDurationTraj = 0.0;
        lastDisplacementTraj = 0.0;
    }

    public TrajectorySequenceBuilder(
		    final Pose2d startPose,
		    final TrajectoryVelocityConstraint baseVelConstraint,
		    final TrajectoryAccelerationConstraint baseAccelConstraint,
		    final double baseTurnConstraintMaxAngVel,
		    final double baseTurnConstraintMaxAngAccel
    ) {
        this(
                startPose, null,
                baseVelConstraint, baseAccelConstraint,
                baseTurnConstraintMaxAngVel, baseTurnConstraintMaxAngAccel
        );
    }

    public TrajectorySequenceBuilder lineTo(final Vector2d endPosition) {
        return addPath(() -> currentTrajectoryBuilder.lineTo(endPosition, currentVelConstraint, currentAccelConstraint));
    }

    public TrajectorySequenceBuilder lineTo(
		    final Vector2d endPosition,
		    final TrajectoryVelocityConstraint velConstraint,
		    final TrajectoryAccelerationConstraint accelConstraint
    ) {
        return addPath(() -> currentTrajectoryBuilder.lineTo(endPosition, velConstraint, accelConstraint));
    }

    public TrajectorySequenceBuilder lineToConstantHeading(final Vector2d endPosition) {
        return addPath(() -> currentTrajectoryBuilder.lineToConstantHeading(endPosition, currentVelConstraint, currentAccelConstraint));
    }

    public TrajectorySequenceBuilder lineToConstantHeading(
		    final Vector2d endPosition,
		    final TrajectoryVelocityConstraint velConstraint,
		    final TrajectoryAccelerationConstraint accelConstraint
    ) {
        return addPath(() -> currentTrajectoryBuilder.lineToConstantHeading(endPosition, velConstraint, accelConstraint));
    }

    public TrajectorySequenceBuilder lineToLinearHeading(final Pose2d endPose) {
        return addPath(() -> currentTrajectoryBuilder.lineToLinearHeading(endPose, currentVelConstraint, currentAccelConstraint));
    }

    public TrajectorySequenceBuilder lineToLinearHeading(
		    final Pose2d endPose,
		    final TrajectoryVelocityConstraint velConstraint,
		    final TrajectoryAccelerationConstraint accelConstraint
    ) {
        return addPath(() -> currentTrajectoryBuilder.lineToLinearHeading(endPose, velConstraint, accelConstraint));
    }

    public TrajectorySequenceBuilder lineToSplineHeading(final Pose2d endPose) {
        return addPath(() -> currentTrajectoryBuilder.lineToSplineHeading(endPose, currentVelConstraint, currentAccelConstraint));
    }

    public TrajectorySequenceBuilder lineToSplineHeading(
		    final Pose2d endPose,
		    final TrajectoryVelocityConstraint velConstraint,
		    final TrajectoryAccelerationConstraint accelConstraint
    ) {
        return addPath(() -> currentTrajectoryBuilder.lineToSplineHeading(endPose, velConstraint, accelConstraint));
    }

    public TrajectorySequenceBuilder strafeTo(final Vector2d endPosition) {
        return addPath(() -> currentTrajectoryBuilder.strafeTo(endPosition, currentVelConstraint, currentAccelConstraint));
    }

    public TrajectorySequenceBuilder strafeTo(
		    final Vector2d endPosition,
		    final TrajectoryVelocityConstraint velConstraint,
		    final TrajectoryAccelerationConstraint accelConstraint
    ) {
        return addPath(() -> currentTrajectoryBuilder.strafeTo(endPosition, velConstraint, accelConstraint));
    }

    public TrajectorySequenceBuilder forward(final double distance) {
        return addPath(() -> currentTrajectoryBuilder.forward(distance, currentVelConstraint, currentAccelConstraint));
    }

    public TrajectorySequenceBuilder forward(
		    final double distance,
		    final TrajectoryVelocityConstraint velConstraint,
		    final TrajectoryAccelerationConstraint accelConstraint
    ) {
        return addPath(() -> currentTrajectoryBuilder.forward(distance, velConstraint, accelConstraint));
    }

    public TrajectorySequenceBuilder back(final double distance) {
        return addPath(() -> currentTrajectoryBuilder.back(distance, currentVelConstraint, currentAccelConstraint));
    }

    public TrajectorySequenceBuilder back(
		    final double distance,
		    final TrajectoryVelocityConstraint velConstraint,
		    final TrajectoryAccelerationConstraint accelConstraint
    ) {
        return addPath(() -> currentTrajectoryBuilder.back(distance, velConstraint, accelConstraint));
    }

    public TrajectorySequenceBuilder strafeLeft(final double distance) {
        return addPath(() -> currentTrajectoryBuilder.strafeLeft(distance, currentVelConstraint, currentAccelConstraint));
    }

    public TrajectorySequenceBuilder strafeLeft(
		    final double distance,
		    final TrajectoryVelocityConstraint velConstraint,
		    final TrajectoryAccelerationConstraint accelConstraint
    ) {
        return addPath(() -> currentTrajectoryBuilder.strafeLeft(distance, velConstraint, accelConstraint));
    }

    public TrajectorySequenceBuilder strafeRight(final double distance) {
        return addPath(() -> currentTrajectoryBuilder.strafeRight(distance, currentVelConstraint, currentAccelConstraint));
    }

    public TrajectorySequenceBuilder strafeRight(
		    final double distance,
		    final TrajectoryVelocityConstraint velConstraint,
		    final TrajectoryAccelerationConstraint accelConstraint
    ) {
        return addPath(() -> currentTrajectoryBuilder.strafeRight(distance, velConstraint, accelConstraint));
    }

    public TrajectorySequenceBuilder splineTo(final Vector2d endPosition, final double endHeading) {
        return addPath(() -> currentTrajectoryBuilder.splineTo(endPosition, endHeading, currentVelConstraint, currentAccelConstraint));
    }

    public TrajectorySequenceBuilder splineTo(
		    final Vector2d endPosition,
		    final double endHeading,
		    final TrajectoryVelocityConstraint velConstraint,
		    final TrajectoryAccelerationConstraint accelConstraint
    ) {
        return addPath(() -> currentTrajectoryBuilder.splineTo(endPosition, endHeading, velConstraint, accelConstraint));
    }

    public TrajectorySequenceBuilder splineToConstantHeading(final Vector2d endPosition, final double endHeading) {
        return addPath(() -> currentTrajectoryBuilder.splineToConstantHeading(endPosition, endHeading, currentVelConstraint, currentAccelConstraint));
    }

    public TrajectorySequenceBuilder splineToConstantHeading(
		    final Vector2d endPosition,
		    final double endHeading,
		    final TrajectoryVelocityConstraint velConstraint,
		    final TrajectoryAccelerationConstraint accelConstraint
    ) {
        return addPath(() -> currentTrajectoryBuilder.splineToConstantHeading(endPosition, endHeading, velConstraint, accelConstraint));
    }

    public TrajectorySequenceBuilder splineToLinearHeading(final Pose2d endPose, final double endHeading) {
        return addPath(() -> currentTrajectoryBuilder.splineToLinearHeading(endPose, endHeading, currentVelConstraint, currentAccelConstraint));
    }

    public TrajectorySequenceBuilder splineToLinearHeading(
		    final Pose2d endPose,
		    final double endHeading,
		    final TrajectoryVelocityConstraint velConstraint,
		    final TrajectoryAccelerationConstraint accelConstraint
    ) {
        return addPath(() -> currentTrajectoryBuilder.splineToLinearHeading(endPose, endHeading, velConstraint, accelConstraint));
    }

    public TrajectorySequenceBuilder splineToSplineHeading(final Pose2d endPose, final double endHeading) {
        return addPath(() -> currentTrajectoryBuilder.splineToSplineHeading(endPose, endHeading, currentVelConstraint, currentAccelConstraint));
    }

    public TrajectorySequenceBuilder splineToSplineHeading(
		    final Pose2d endPose,
		    final double endHeading,
		    final TrajectoryVelocityConstraint velConstraint,
		    final TrajectoryAccelerationConstraint accelConstraint
    ) {
        return addPath(() -> currentTrajectoryBuilder.splineToSplineHeading(endPose, endHeading, velConstraint, accelConstraint));
    }

    private TrajectorySequenceBuilder addPath(final AddPathCallback callback) {
        if (null == currentTrajectoryBuilder) newPath();

        try {
            callback.run();
        } catch (final PathContinuityViolationException e) {
            newPath();
            callback.run();
        }

        final Trajectory builtTraj = currentTrajectoryBuilder.build();

        final double durationDifference = builtTraj.duration() - lastDurationTraj;
        final double displacementDifference = builtTraj.getPath().length() - lastDisplacementTraj;

        lastPose = builtTraj.end();
        currentDuration += durationDifference;
        currentDisplacement += displacementDifference;

        lastDurationTraj = builtTraj.duration();
        lastDisplacementTraj = builtTraj.getPath().length();

        return this;
    }

    public TrajectorySequenceBuilder setTangent(final double tangent) {
        setAbsoluteTangent = true;
        absoluteTangent = tangent;

        pushPath();

        return this;
    }

    private TrajectorySequenceBuilder setTangentOffset(final double offset) {
        setAbsoluteTangent = false;

        this.tangentOffset = offset;
        this.pushPath();

        return this;
    }

    public TrajectorySequenceBuilder setReversed(final boolean reversed) {
        return reversed ? this.setTangentOffset(Math.toRadians(180.0)) : this.setTangentOffset(0.0);
    }

    public TrajectorySequenceBuilder setConstraints(
		    final TrajectoryVelocityConstraint velConstraint,
		    final TrajectoryAccelerationConstraint accelConstraint
    ) {
        this.currentVelConstraint = velConstraint;
        this.currentAccelConstraint = accelConstraint;

        return this;
    }

    public TrajectorySequenceBuilder resetConstraints() {
        this.currentVelConstraint = this.baseVelConstraint;
        this.currentAccelConstraint = this.baseAccelConstraint;

        return this;
    }

    public TrajectorySequenceBuilder setVelConstraint(final TrajectoryVelocityConstraint velConstraint) {
        this.currentVelConstraint = velConstraint;

        return this;
    }

    public TrajectorySequenceBuilder resetVelConstraint() {
        this.currentVelConstraint = this.baseVelConstraint;

        return this;
    }

    public TrajectorySequenceBuilder setAccelConstraint(final TrajectoryAccelerationConstraint accelConstraint) {
        this.currentAccelConstraint = accelConstraint;

        return this;
    }

    public TrajectorySequenceBuilder resetAccelConstraint() {
        this.currentAccelConstraint = this.baseAccelConstraint;

        return this;
    }

    public TrajectorySequenceBuilder setTurnConstraint(final double maxAngVel, final double maxAngAccel) {
        this.currentTurnConstraintMaxAngVel = maxAngVel;
        this.currentTurnConstraintMaxAngAccel = maxAngAccel;

        return this;
    }

    public TrajectorySequenceBuilder resetTurnConstraint() {
        this.currentTurnConstraintMaxAngVel = baseTurnConstraintMaxAngVel;
        this.currentTurnConstraintMaxAngAccel = baseTurnConstraintMaxAngAccel;

        return this;
    }

    public TrajectorySequenceBuilder addTemporalMarker(final MarkerCallback callback) {
        return this.addTemporalMarker(currentDuration, callback);
    }

    public TrajectorySequenceBuilder UNSTABLE_addTemporalMarkerOffset(final double offset, final MarkerCallback callback) {
        return this.addTemporalMarker(currentDuration + offset, callback);
    }

    public TrajectorySequenceBuilder addTemporalMarker(final double time, final MarkerCallback callback) {
        return this.addTemporalMarker(0.0, time, callback);
    }

    public TrajectorySequenceBuilder addTemporalMarker(final double scale, final double offset, final MarkerCallback callback) {
        return this.addTemporalMarker(time -> scale * time + offset, callback);
    }

    public TrajectorySequenceBuilder addTemporalMarker(final TimeProducer time, final MarkerCallback callback) {
        this.temporalMarkers.add(new TemporalMarker(time, callback));
        return this;
    }

    public TrajectorySequenceBuilder addSpatialMarker(final Vector2d point, final MarkerCallback callback) {
        this.spatialMarkers.add(new SpatialMarker(point, callback));
        return this;
    }

    public TrajectorySequenceBuilder addDisplacementMarker(final MarkerCallback callback) {
        return this.addDisplacementMarker(currentDisplacement, callback);
    }

    public TrajectorySequenceBuilder UNSTABLE_addDisplacementMarkerOffset(final double offset, final MarkerCallback callback) {
        return this.addDisplacementMarker(currentDisplacement + offset, callback);
    }

    public TrajectorySequenceBuilder addDisplacementMarker(final double displacement, final MarkerCallback callback) {
        return this.addDisplacementMarker(0.0, displacement, callback);
    }

    public TrajectorySequenceBuilder addDisplacementMarker(final double scale, final double offset, final MarkerCallback callback) {
        return addDisplacementMarker((displacement -> scale * displacement + offset), callback);
    }

    public TrajectorySequenceBuilder addDisplacementMarker(final DisplacementProducer displacement, final MarkerCallback callback) {
        displacementMarkers.add(new DisplacementMarker(displacement, callback));

        return this;
    }

    public TrajectorySequenceBuilder turn(final double angle) {
        return turn(angle, currentTurnConstraintMaxAngVel, currentTurnConstraintMaxAngAccel);
    }

    public TrajectorySequenceBuilder turn(final double angle, final double maxAngVel, final double maxAngAccel) {
        pushPath();

        final MotionProfile turnProfile = MotionProfileGenerator.generateSimpleMotionProfile(
                new MotionState(lastPose.getHeading(), 0.0, 0.0, 0.0),
                new MotionState(lastPose.getHeading() + angle, 0.0, 0.0, 0.0),
                maxAngVel,
                maxAngAccel
        );

        sequenceSegments.add(new TurnSegment(lastPose, angle, turnProfile, Collections.emptyList()));

        lastPose = new Pose2d(
                lastPose.getX(), lastPose.getY(),
                Angle.norm(lastPose.getHeading() + angle)
        );

        currentDuration += turnProfile.duration();

        return this;
    }

    public TrajectorySequenceBuilder waitSeconds(final double seconds) {
        pushPath();
        sequenceSegments.add(new WaitSegment(lastPose, seconds, Collections.emptyList()));

        currentDuration += seconds;
        return this;
    }

    public TrajectorySequenceBuilder addTrajectory(final Trajectory trajectory) {
        pushPath();

        sequenceSegments.add(new TrajectorySegment(trajectory));
        return this;
    }

    private void pushPath() {
        if (null != currentTrajectoryBuilder) {
            final Trajectory builtTraj = currentTrajectoryBuilder.build();
            sequenceSegments.add(new TrajectorySegment(builtTraj));
        }

        currentTrajectoryBuilder = null;
    }

    private void newPath() {
        if (null != currentTrajectoryBuilder)
            pushPath();

        lastDurationTraj = 0.0;
        lastDisplacementTraj = 0.0;

        final double tangent = setAbsoluteTangent ? absoluteTangent : Angle.norm(lastPose.getHeading() + tangentOffset);

        currentTrajectoryBuilder = new TrajectoryBuilder(lastPose, tangent, currentVelConstraint, currentAccelConstraint, resolution);
    }

    public TrajectorySequence build() {
        pushPath();

        final List<TrajectoryMarker> globalMarkers = convertMarkersToGlobal(
                sequenceSegments,
                temporalMarkers, displacementMarkers, spatialMarkers
        );
        projectGlobalMarkersToLocalSegments(globalMarkers, sequenceSegments);

        return new TrajectorySequence(sequenceSegments);
    }

    private List<TrajectoryMarker> convertMarkersToGlobal(
		    final List<SequenceSegment> sequenceSegments,
		    final List<TemporalMarker> temporalMarkers,
		    final List<DisplacementMarker> displacementMarkers,
		    final List<SpatialMarker> spatialMarkers
    ) {
        final ArrayList<TrajectoryMarker> trajectoryMarkers = new ArrayList<>();

        // Convert temporal markers
        for (final TemporalMarker marker : temporalMarkers) {
            trajectoryMarkers.add(
                    new TrajectoryMarker(marker.getProducer().produce(currentDuration), marker.getCallback())
            );
        }

        // Convert displacement markers
        for (final DisplacementMarker marker : displacementMarkers) {
            final double time = displacementToTime(
                    sequenceSegments,
                    marker.getProducer().produce(currentDisplacement)
            );

            trajectoryMarkers.add(
                    new TrajectoryMarker(
                            time,
                            marker.getCallback()
                    )
            );
        }

        // Convert spatial markers
        for (final SpatialMarker marker : spatialMarkers) {
            trajectoryMarkers.add(
                    new TrajectoryMarker(
                            pointToTime(sequenceSegments, marker.getPoint()),
                            marker.getCallback()
                    )
            );
        }

        return trajectoryMarkers;
    }

    private void projectGlobalMarkersToLocalSegments(final List<TrajectoryMarker> markers, final List<SequenceSegment> sequenceSegments) {
        if (sequenceSegments.isEmpty()) return;

        markers.sort(Comparator.comparingDouble(TrajectoryMarker::getTime));

        double timeOffset = 0.0;
        int markerIndex = 0;
        for (final SequenceSegment segment : sequenceSegments) {
            while (markerIndex < markers.size()) {
                final TrajectoryMarker marker = markers.get(markerIndex);
                if (marker.getTime() >= timeOffset + segment.getDuration()) {
                    break;
                }

                segment.getMarkers().add(new TrajectoryMarker(
                        Math.max(0.0, marker.getTime()) - timeOffset, marker.getCallback()));
                ++markerIndex;
            }

            timeOffset += segment.getDuration();
        }

        final SequenceSegment segment = sequenceSegments.get(sequenceSegments.size() - 1);
        while (markerIndex < markers.size()) {
            final TrajectoryMarker marker = markers.get(markerIndex);
            segment.getMarkers().add(new TrajectoryMarker(segment.getDuration(), marker.getCallback()));
            ++markerIndex;
        }
    }

    // Taken from Road Runner's TrajectoryGenerator.displacementToTime() since it's private
    // note: this assumes that the profile position is monotonic increasing
    private Double motionProfileDisplacementToTime(final MotionProfile profile, final double s) {
        double tLo = 0.0;
        double tHi = profile.duration();
        while (!(1e-6 > Math.abs(tLo - tHi))) {
            final double tMid = 0.5 * (tLo + tHi);
            if (profile.get(tMid).getX() > s) {
                tHi = tMid;
            } else {
                tLo = tMid;
            }
        }
        return 0.5 * (tLo + tHi);
    }

    private Double displacementToTime(final List<SequenceSegment> sequenceSegments, final double s) {
        double currentTime = 0.0;
        double currentDisplacement = 0.0;

        for (final SequenceSegment segment : sequenceSegments) {
            if (segment instanceof TrajectorySegment) {
                final TrajectorySegment thisSegment = (TrajectorySegment) segment;

                final double segmentLength = thisSegment.getTrajectory().getPath().length();

                if (currentDisplacement + segmentLength > s) {
                    final double target = s - currentDisplacement;
                    final double timeInSegment = motionProfileDisplacementToTime(
                            thisSegment.getTrajectory().getProfile(),
                            target
                    );

                    return currentTime + timeInSegment;
                } else {
                    currentDisplacement += segmentLength;
                }
            }

            currentTime += segment.getDuration();
        }

        return currentTime;
    }

    private Double pointToTime(final List<SequenceSegment> sequenceSegments, final Vector2d point) {
        class ComparingPoints {
            private final double distanceToPoint;
            private final double totalDisplacement;
            private final double thisPathDisplacement;

            public ComparingPoints(final double distanceToPoint, final double totalDisplacement, final double thisPathDisplacement) {
                this.distanceToPoint = distanceToPoint;
                this.totalDisplacement = totalDisplacement;
                this.thisPathDisplacement = thisPathDisplacement;
            }
        }

        final List<ComparingPoints> projectedPoints = new ArrayList<>();

        for (final SequenceSegment segment : sequenceSegments) {
            if (segment instanceof TrajectorySegment) {
                final TrajectorySegment thisSegment = (TrajectorySegment) segment;

                final double displacement = thisSegment.getTrajectory().getPath().project(point, 0.25);
                final Vector2d projectedPoint = thisSegment.getTrajectory().getPath().get(displacement).vec();
                final double distanceToPoint = point.minus(projectedPoint).norm();

                double totalDisplacement = 0.0;

                for (final ComparingPoints comparingPoint : projectedPoints) {
                    totalDisplacement += comparingPoint.totalDisplacement;
                }

                totalDisplacement += displacement;

                projectedPoints.add(new ComparingPoints(distanceToPoint, displacement, totalDisplacement));
            }
        }

        ComparingPoints closestPoint = null;

        for (final ComparingPoints comparingPoint : projectedPoints) {
            if (null == closestPoint) {
                closestPoint = comparingPoint;
                continue;
            }

            if (comparingPoint.distanceToPoint < closestPoint.distanceToPoint)
                closestPoint = comparingPoint;
        }

        return displacementToTime(sequenceSegments, closestPoint.thisPathDisplacement);
    }

    private interface AddPathCallback {
        void run();
    }
}
