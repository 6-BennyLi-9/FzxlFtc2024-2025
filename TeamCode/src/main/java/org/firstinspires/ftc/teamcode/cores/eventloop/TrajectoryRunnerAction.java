package org.firstinspires.ftc.teamcode.cores.eventloop;

import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.acmerobotics.roadrunner.SampleMecanumDrive;
import org.acmerobotics.roadrunner.trajectorysequence.TrajectorySequence;
import org.betastudio.ftc.action.ActionImplementFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class TrajectoryRunnerAction extends ActionImplementFactory {
	public TrajectoryRunnerAction(SampleMecanumDrive drive, TrajectorySequence trajectorySequence) {
		AtomicBoolean isInitialized = new AtomicBoolean(false);
		setAction(() -> {
			if (!isInitialized.get()){
				isInitialized.set(true);
				drive.followTrajectorySequenceAsync(trajectorySequence);
			}
			drive.update();
			return drive.isBusy();
		});
	}
	public TrajectoryRunnerAction(SampleMecanumDrive drive, Trajectory trajectory) {
		AtomicBoolean isInitialized = new AtomicBoolean(false);
		setAction(() -> {
			if (!isInitialized.get()){
				isInitialized.set(true);
				drive.followTrajectoryAsync(trajectory);
			}
			drive.update();
			return drive.isBusy();
		});
	}
}
