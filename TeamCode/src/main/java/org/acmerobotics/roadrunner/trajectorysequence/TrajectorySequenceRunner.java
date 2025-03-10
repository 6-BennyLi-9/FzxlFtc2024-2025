package org.acmerobotics.roadrunner.trajectorysequence;

import androidx.annotation.Nullable;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.control.PIDFController;
import com.acmerobotics.roadrunner.drive.DriveSignal;
import com.acmerobotics.roadrunner.followers.TrajectoryFollower;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.profile.MotionState;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryMarker;
import com.acmerobotics.roadrunner.util.NanoClock;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.acmerobotics.roadrunner.DriveConstants;
import org.acmerobotics.roadrunner.trajectorysequence.sequencesegment.SequenceSegment;
import org.acmerobotics.roadrunner.trajectorysequence.sequencesegment.TrajectorySegment;
import org.acmerobotics.roadrunner.trajectorysequence.sequencesegment.TurnSegment;
import org.acmerobotics.roadrunner.trajectorysequence.sequencesegment.WaitSegment;
import org.acmerobotics.roadrunner.util.DashboardUtil;
import org.acmerobotics.roadrunner.util.LogFiles;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

//@Config
public class TrajectorySequenceRunner {
	public static final String COLOR_INACTIVE_TRAJECTORY = "#4caf507a";
	public static final String COLOR_INACTIVE_TURN       = "#7c4dff7a";
	public static final String COLOR_INACTIVE_WAIT       = "#dd2c007a";

	public static final String COLOR_ACTIVE_TRAJECTORY = "#4CAF50";
	public static final String COLOR_ACTIVE_TURN       = "#7c4dff";
	public static final String COLOR_ACTIVE_WAIT       = "#dd2c00";

	public static final int POSE_HISTORY_LIMIT = 100;

	private final TrajectoryFollower follower;

	private final PIDFController turnController;

	private final NanoClock           clock;
	private final FtcDashboard        dashboard;
	private final LinkedList <Pose2d> poseHistory = new LinkedList <>();
	private final VoltageSensor       voltageSensor;
	private final List <Integer>      lastDriveEncPositions;
	private final List <Integer>      lastDriveEncVels;
	private final List <Integer>      lastTrackingEncPositions;
	private final List <Integer>          lastTrackingEncVels;
	final         List <TrajectoryMarker> remainingMarkers = new ArrayList <>();
	private       TrajectorySequence      currentTrajectorySequence;
	private double             currentSegmentStartTime;
	private int                currentSegmentIndex;
	private int                lastSegmentIndex;
	private Pose2d             lastPoseError = new Pose2d();

	public TrajectorySequenceRunner(final TrajectoryFollower follower, final PIDCoefficients headingPIDCoefficients, final VoltageSensor voltageSensor, final List <Integer> lastDriveEncPositions, final List <Integer> lastDriveEncVels, final List <Integer> lastTrackingEncPositions, final List <Integer> lastTrackingEncVels) {
		this.follower = follower;

		turnController = new PIDFController(headingPIDCoefficients);
		turnController.setInputBounds(0, 2 * Math.PI);

		this.voltageSensor = voltageSensor;

		this.lastDriveEncPositions = lastDriveEncPositions;
		this.lastDriveEncVels = lastDriveEncVels;
		this.lastTrackingEncPositions = lastTrackingEncPositions;
		this.lastTrackingEncVels = lastTrackingEncVels;

		clock = NanoClock.system();

		dashboard = FtcDashboard.getInstance();
		dashboard.setTelemetryTransmissionInterval(25);
	}

	public void followTrajectorySequenceAsync(final TrajectorySequence trajectorySequence) {
		currentTrajectorySequence = trajectorySequence;
		currentSegmentStartTime = clock.seconds();
		currentSegmentIndex = 0;
		lastSegmentIndex = - 1;
	}

	public @Nullable DriveSignal update(final Pose2d poseEstimate, final Pose2d poseVelocity) {
		Pose2d      targetPose  = null;
		DriveSignal driveSignal = null;

		final TelemetryPacket packet       = new TelemetryPacket();
		final Canvas          fieldOverlay = packet.fieldOverlay();

		SequenceSegment currentSegment = null;

		if (null != currentTrajectorySequence) {
			if (currentSegmentIndex >= currentTrajectorySequence.size()) {
				for (final TrajectoryMarker marker : remainingMarkers) {
					marker.getCallback().onMarkerReached();
				}

				remainingMarkers.clear();

				currentTrajectorySequence = null;
			}

			if (null == currentTrajectorySequence) return new DriveSignal();

			final double  now             = clock.seconds();
			final boolean isNewTransition = currentSegmentIndex != lastSegmentIndex;

			currentSegment = currentTrajectorySequence.get(currentSegmentIndex);

			if (isNewTransition) {
				currentSegmentStartTime = now;
				lastSegmentIndex = currentSegmentIndex;

				for (final TrajectoryMarker marker : remainingMarkers) {
					marker.getCallback().onMarkerReached();
				}

				remainingMarkers.clear();

				remainingMarkers.addAll(currentSegment.getMarkers());
				remainingMarkers.sort(Comparator.comparingDouble(TrajectoryMarker::getTime));
			}

			final double deltaTime = now - currentSegmentStartTime;

			if (currentSegment instanceof TrajectorySegment) {
				final Trajectory currentTrajectory = ((TrajectorySegment) currentSegment).getTrajectory();

				if (isNewTransition) follower.followTrajectory(currentTrajectory);

				if (! follower.isFollowing()) {
					currentSegmentIndex++;

					driveSignal = new DriveSignal();
				} else {
					driveSignal = follower.update(poseEstimate, poseVelocity);
					lastPoseError = follower.getLastError();
				}

				targetPose = currentTrajectory.get(deltaTime);
			} else if (currentSegment instanceof TurnSegment) {
				final MotionState targetState = ((TurnSegment) currentSegment).getMotionProfile().get(deltaTime);

				turnController.setTargetPosition(targetState.getX());

				final double correction = turnController.update(poseEstimate.getHeading());

				final double targetOmega = targetState.getV();
				final double targetAlpha = targetState.getA();

				lastPoseError = new Pose2d(0, 0, turnController.getLastError());

				final Pose2d startPose = currentSegment.getStartPose();
				targetPose = startPose.copy(startPose.getX(), startPose.getY(), targetState.getX());

				driveSignal = new DriveSignal(new Pose2d(0, 0, targetOmega + correction), new Pose2d(0, 0, targetAlpha));

				if (deltaTime >= currentSegment.getDuration()) {
					currentSegmentIndex++;
					driveSignal = new DriveSignal();
				}
			} else if (currentSegment instanceof WaitSegment) {
				lastPoseError = new Pose2d();

				targetPose = currentSegment.getStartPose();
				driveSignal = new DriveSignal();

				if (deltaTime >= currentSegment.getDuration()) {
					currentSegmentIndex++;
				}
			}

			while (! remainingMarkers.isEmpty() && deltaTime > remainingMarkers.get(0).getTime()) {
				remainingMarkers.get(0).getCallback().onMarkerReached();
				remainingMarkers.remove(0);
			}
		}

		poseHistory.add(poseEstimate);

		if (- 1 < POSE_HISTORY_LIMIT && POSE_HISTORY_LIMIT < poseHistory.size()) {
			poseHistory.removeFirst();
		}

		final double NOMINAL_VOLTAGE = 12.0;
		final double voltage         = voltageSensor.getVoltage();
		if (null != driveSignal && ! DriveConstants.RUN_USING_ENCODER) {
			driveSignal = new DriveSignal(driveSignal.getVel().times(NOMINAL_VOLTAGE / voltage), driveSignal.getAccel().times(NOMINAL_VOLTAGE / voltage));
		}

		if (null != targetPose) {
			LogFiles.record(targetPose, poseEstimate, voltage, lastDriveEncPositions, lastDriveEncVels, lastTrackingEncPositions, lastTrackingEncVels);
		}

		packet.put("x", poseEstimate.getX());
		packet.put("y", poseEstimate.getY());
		packet.put("heading (deg)", Math.toDegrees(poseEstimate.getHeading()));

		packet.put("xError", lastPoseError.getX());
		packet.put("yError", lastPoseError.getY());
		packet.put("headingError (deg)", Math.toDegrees(lastPoseError.getHeading()));

		draw(fieldOverlay, currentTrajectorySequence, currentSegment, targetPose, poseEstimate);

		dashboard.sendTelemetryPacket(packet);

		return driveSignal;
	}

	private void draw(final Canvas fieldOverlay, final TrajectorySequence sequence, final SequenceSegment currentSegment, final Pose2d targetPose, final Pose2d poseEstimate) {
		if (null != sequence) {
			for (int i = 0 ; i < sequence.size() ; i++) {
				final SequenceSegment segment = sequence.get(i);

				if (segment instanceof TrajectorySegment) {
					fieldOverlay.setStrokeWidth(1);
					fieldOverlay.setStroke(COLOR_INACTIVE_TRAJECTORY);

					DashboardUtil.drawSampledPath(fieldOverlay, ((TrajectorySegment) segment).getTrajectory().getPath());
				} else if (segment instanceof TurnSegment) {
					final Pose2d pose = segment.getStartPose();

					fieldOverlay.setFill(COLOR_INACTIVE_TURN);
					fieldOverlay.fillCircle(pose.getX(), pose.getY(), 2);
				} else if (segment instanceof WaitSegment) {
					final Pose2d pose = segment.getStartPose();

					fieldOverlay.setStrokeWidth(1);
					fieldOverlay.setStroke(COLOR_INACTIVE_WAIT);
					fieldOverlay.strokeCircle(pose.getX(), pose.getY(), 3);
				}
			}
		}

		if (null != currentSegment) {
			if (currentSegment instanceof TrajectorySegment) {
				final Trajectory currentTrajectory = ((TrajectorySegment) currentSegment).getTrajectory();

				fieldOverlay.setStrokeWidth(1);
				fieldOverlay.setStroke(COLOR_ACTIVE_TRAJECTORY);

				DashboardUtil.drawSampledPath(fieldOverlay, currentTrajectory.getPath());
			} else if (currentSegment instanceof TurnSegment) {
				final Pose2d pose = currentSegment.getStartPose();

				fieldOverlay.setFill(COLOR_ACTIVE_TURN);
				fieldOverlay.fillCircle(pose.getX(), pose.getY(), 3);
			} else if (currentSegment instanceof WaitSegment) {
				final Pose2d pose = currentSegment.getStartPose();

				fieldOverlay.setStrokeWidth(1);
				fieldOverlay.setStroke(COLOR_ACTIVE_WAIT);
				fieldOverlay.strokeCircle(pose.getX(), pose.getY(), 3);
			}
		}

		if (null != targetPose) {
			fieldOverlay.setStrokeWidth(1);
			fieldOverlay.setStroke("#4CAF50");
			DashboardUtil.drawRobot(fieldOverlay, targetPose);
		}

		fieldOverlay.setStroke("#3F51B5");
		DashboardUtil.drawPoseHistory(fieldOverlay, poseHistory);

		fieldOverlay.setStroke("#3F51B5");
		DashboardUtil.drawRobot(fieldOverlay, poseEstimate);
	}

	public Pose2d getLastPoseError() {
		return lastPoseError;
	}

	public boolean isBusy() {
		return null != currentTrajectorySequence;
	}
}
