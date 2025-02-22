package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.robotcore.external.Const;
import org.firstinspires.ftc.teamcode.roadrunner.sequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.roadrunner.sequence.TrajectorySequenceBuilder;

public abstract class LinearEventMode extends TeamLinearMode {
	public TrajectorySequence        MAIN_SEQUENCE;
	public TrajectorySequenceBuilder MAIN_BUILDER;

	@Override
	public void onRunning(){
		drive.setPoseEstimate(getInitialPoseEstimate());
		MAIN_BUILDER = drive.trajectorySequenceBuilder(getInitialPoseEstimate());

		initialize();
		MAIN_SEQUENCE=MAIN_BUILDER.build();

		telemetry.addData("Status","初始化已完成");
		telemetry.update();

		waitForStart();
		if (isStopRequested()) {
			return;
		}
		drive.followTrajectorySequence(MAIN_SEQUENCE);
	}

	/**
	 * 要求：修改 MAIN_BUILDER
	 */
	public abstract void initialize();
	@Const
	public abstract Pose2d getInitialPoseEstimate();
}
