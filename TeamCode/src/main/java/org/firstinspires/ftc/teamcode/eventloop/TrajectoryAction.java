package org.firstinspires.ftc.teamcode.eventloop;

import static org.betastudio.ftc.util.Pose2dUtil.str;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.acmerobotics.roadrunner.SampleMecanumDrive;
import org.acmerobotics.roadrunner.trajectorysequence.TrajectorySequence;
import org.betastudio.ftc.action.ActionImplementFactory;
import org.betastudio.ftc.ui.client.Client;

import java.util.concurrent.atomic.AtomicBoolean;

public class TrajectoryAction extends ActionImplementFactory {
	private static Client client;

	public TrajectoryAction(final SampleMecanumDrive drive, final TrajectorySequence trajectorySequence) {
		final AtomicBoolean isInitialized = new AtomicBoolean(false);
		setAction(() -> {
			if (! isInitialized.get()) {
				isInitialized.set(true);
				drive.followTrajectorySequenceAsync(trajectorySequence);
			}
			drive.update();
			Pose2d minus = trajectorySequence.end().minus(drive.getPoseEstimate());
			client.putData("点位差", str(minus));
			return drive.isBusy();
		});
	}

	public TrajectoryAction(final SampleMecanumDrive drive, final Trajectory trajectory) {
		final AtomicBoolean isInitialized = new AtomicBoolean(false);
		setAction(() -> {
			if (! isInitialized.get()) {
				isInitialized.set(true);
				drive.followTrajectoryAsync(trajectory);
			}
			drive.update();
			Pose2d minus = trajectory.end().minus(drive.getPoseEstimate());
			client.putData("点位差", str(minus));
			return drive.isBusy();
		});
	}

	public static void setClient(final Client client) {
		TrajectoryAction.client = client;
	}
}
