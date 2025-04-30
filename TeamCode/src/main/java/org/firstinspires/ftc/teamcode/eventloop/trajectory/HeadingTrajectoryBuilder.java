package org.firstinspires.ftc.teamcode.eventloop.trajectory;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.acmerobotics.roadrunner.SampleMecanumDrive;
import org.betastudio.ftc.action.Action;

public class HeadingTrajectoryBuilder {
	private final SampleMecanumDrive drive;
	private       Pose2d             current;

	public HeadingTrajectoryBuilder(@NonNull final SampleMecanumDrive drive) {
		this.drive = drive;
		current = drive.getPoseEstimate();
	}

	public Trajectory lineTo(final Pose2d end) {
		final Trajectory build = drive.trajectoryBuilder(current).lineToLinearHeading(end).build();
		current = end;
		return build;
	}

	public Action runTo(final Pose2d end) {
		return new TrajectoryAction(drive, lineTo(end));
	}

	public void setCurrent(final Pose2d current) {
		this.current = current;
	}
}
