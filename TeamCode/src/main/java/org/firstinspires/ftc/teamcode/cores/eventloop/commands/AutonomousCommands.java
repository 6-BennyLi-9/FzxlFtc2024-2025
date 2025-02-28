package org.firstinspires.ftc.teamcode.cores.eventloop.commands;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.acmerobotics.roadrunner.SampleMecanumDrive;
import org.acmerobotics.roadrunner.trajectorysequence.TrajectorySequence;
import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.Actions;
import org.betastudio.ftc.action.utils.StatementAction;

public final class AutonomousCommands {
	public interface Command{
	}

	public static class TrajectoryCommand implements Command{
		private final Trajectory trajectory;

		public TrajectoryCommand(Trajectory trajectory) {
			this.trajectory = trajectory;
		}

		public void execute(@NonNull SampleMecanumDrive driver){
			driver.followTrajectoryAsync(trajectory);
		}
	}

	public static class TrajectorySequenceCommand implements Command{
		private final TrajectorySequence trajectory;

		public TrajectorySequenceCommand(TrajectorySequence trajectory) {
			this.trajectory = trajectory;
		}

		public void execute(@NonNull SampleMecanumDrive driver){
			driver.followTrajectorySequenceAsync(trajectory);
		}
	}

	public static class ActionCommand implements Command{
		private final Action action;

		public ActionCommand(Action action) {
			this.action = action;
		}

		public ActionCommand(Runnable runnable){
			this.action = new StatementAction(runnable);
		}

		public void execute(){
			Actions.runAction(action);
		}
	}
}
