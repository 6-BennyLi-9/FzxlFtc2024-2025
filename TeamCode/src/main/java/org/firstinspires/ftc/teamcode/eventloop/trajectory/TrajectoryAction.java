package org.firstinspires.ftc.teamcode.eventloop.trajectory;

import static org.betastudio.ftc.Interfaces.ProgressMarker;
import static org.betastudio.ftc.Interfaces.ProgressedTask;
import static org.betastudio.ftc.util.Pose2dUtil.str;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.acmerobotics.roadrunner.SampleMecanumDrive;
import org.acmerobotics.roadrunner.trajectorysequence.TrajectorySequence;
import org.betastudio.ftc.action.AbstractActionImplement;
import org.betastudio.ftc.ui.client.Client;
import org.betastudio.ftc.util.Pose2dUtil;
import org.betastudio.ftc.util.ProgressMarkerImplement;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 不会自动调用 {@link SampleMecanumDrive#update()}
 */
public class TrajectoryAction extends AbstractActionImplement implements ProgressedTask {
	public static final double PROGRESS_LENGTH_MULTIPLIER = 10;
	private static Client                  client;
	private final  ProgressMarkerImplement marker;

	public TrajectoryAction(@NonNull final SampleMecanumDrive drive, @NonNull final TrajectorySequence track) {
		final AtomicBoolean isInitialized = new AtomicBoolean(false);
		marker = new ProgressMarkerImplement((long) (PROGRESS_LENGTH_MULTIPLIER * Pose2dUtil.dis(track.end().minus(track.start()))));

		setAction(() -> {
			if (! isInitialized.get()) {
				isInitialized.set(true);
				drive.followTrajectorySequenceAsync(track);
			}
			Pose2d minus = track.end().minus(drive.getPoseEstimate());
			marker.setDone((long) (PROGRESS_LENGTH_MULTIPLIER * Pose2dUtil.dis(minus)));
			client.putData("点位差", str(minus));
			return drive.isBusy();
		});
	}

	public TrajectoryAction(@NonNull final SampleMecanumDrive drive, @NonNull final Trajectory track) {
		final AtomicBoolean isInitialized = new AtomicBoolean(false);
		marker = new ProgressMarkerImplement((long) (PROGRESS_LENGTH_MULTIPLIER * Pose2dUtil.dis(track.end().minus(track.start()))));

		setAction(() -> {
			if (! isInitialized.get()) {
				isInitialized.set(true);
				drive.followTrajectoryAsync(track);
			}
			Pose2d minus = track.end().minus(drive.getPoseEstimate());
			marker.setDone((long) (PROGRESS_LENGTH_MULTIPLIER * Pose2dUtil.dis(minus)));
			client.putData("点位差", str(minus));
			return drive.isBusy();
		});
	}

	public static void setClient(final Client client) {
		TrajectoryAction.client = client;
	}

	@Override
	public ProgressMarker getWorkerProgress() {
		return marker;
	}
}
