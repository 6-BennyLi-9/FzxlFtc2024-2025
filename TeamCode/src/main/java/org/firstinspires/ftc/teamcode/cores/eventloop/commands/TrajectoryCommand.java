package org.firstinspires.ftc.teamcode.cores.eventloop.commands;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.acmerobotics.roadrunner.SampleMecanumDrive;

public class TrajectoryCommand implements Command {
	private final Trajectory trajectory;

	public TrajectoryCommand(Trajectory trajectory) {
		this.trajectory = trajectory;
	}

	public void execute(@NonNull SampleMecanumDrive driver) {
		driver.followTrajectoryAsync(trajectory);
	}
}
