package org.firstinspires.ftc.teamcode.cores.eventloop.commands;

import androidx.annotation.NonNull;

import org.acmerobotics.roadrunner.SampleMecanumDrive;
import org.acmerobotics.roadrunner.trajectorysequence.TrajectorySequence;

public class TrajectorySequenceCommand implements Command {
	private final TrajectorySequence trajectory;

	public TrajectorySequenceCommand(TrajectorySequence trajectory) {
		this.trajectory = trajectory;
	}

	public void execute(@NonNull SampleMecanumDrive driver) {
		driver.followTrajectorySequenceAsync(trajectory);
	}
}
