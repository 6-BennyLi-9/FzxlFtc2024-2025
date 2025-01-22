package org.firstinspires.ftc.cores.eventloop;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;

import org.acmerobotics.roadrunner.trajectorysequence.TrajectorySequence;
import org.acmerobotics.roadrunner.trajectorysequence.TrajectorySequenceBuilder;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("UnusedReturnValue")
public abstract class IntegralAutonomous extends IntegralLinearMode {
	private final Map <String, Trajectory>         trajectoryMap           = new HashMap <>();
	private final Map <String, TrajectorySequence> trajectorySequenceMap   = new HashMap <>();

	public abstract void initialize();

	public abstract void linear();

	@Override
	public Thread getLinearThread() {
		return new Thread(
				() -> {
					try {
						initialize();
						waitForStart();
						linear();
					} catch (final Exception e) {
						sendTerminateSignal(TerminateReason.UNCAUGHT_EXCEPTION, e);
					} finally {
						preTerminate();
						sendTerminateSignal(TerminateReason.NATURALLY_SHUT_DOWN);
					}
				}
		);
	}

	public Pose2d registerTrajectory(final String tag, final Trajectory argument) {
		trajectoryMap.put(tag, argument);
		return argument.end();
	}

	public Pose2d registerTrajectory(final String tag, final TrajectorySequence argument) {
		trajectorySequenceMap.put(tag, argument);
		return argument.end();
	}

	public void runTrajectory(final String tag) {
		if (trajectoryMap.containsKey(tag)) {
			drive.followTrajectory(trajectoryMap.get(tag));
		} else {
			drive.followTrajectorySequence(trajectorySequenceMap.get(tag));
		}
	}

	public TrajectoryBuilder generateBuilder(final Pose2d pose) {
		return drive.trajectoryBuilder(pose);
	}

	public TrajectorySequenceBuilder generateSequenceBuilder(final Pose2d pose) {
		return drive.trajectorySequenceBuilder(pose);
	}
}
