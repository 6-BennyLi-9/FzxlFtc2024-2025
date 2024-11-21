package org.firstinspires.ftc.teamcode.autonomous.utils;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.utils.structure.Util;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.client.TelemetryClient;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused","UnusedReturnValue"})
public abstract class IntegralLinearOpMode extends LinearOpMode {
	public SampleMecanumDrive drive;
	public TelemetryClient client;
	public Util utils;

	private final Map<String, Trajectory> trajectoryMap=new HashMap<>();
	private final Map<String, TrajectorySequence> trajectorySequenceMap=new HashMap<>();

	@Override
	public final void runOpMode() throws InterruptedException {
		HardwareConstants.sync(hardwareMap, false);
		drive=new SampleMecanumDrive(hardwareMap);
		telemetry=new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
		client=new TelemetryClient(telemetry);
		utils=new Util();
		initialize();

		TelemetryClient.getInstance().addLine(">>>ROBOT READY!");

		waitForStart();

		TelemetryClient.getInstance().deleteLine(">>>ROBOT READY!");

		if(isStopRequested())return;
		linear();
	}

	public abstract void initialize();
	public abstract void linear();

	public Pose2d registerTrajectory(final String tag, final Trajectory argument){
		trajectoryMap.put(tag,argument);
		return argument.end();
	}
	public Pose2d registerTrajectory(final String tag, final TrajectorySequence argument){
		trajectorySequenceMap.put(tag,argument);
		return argument.end();
	}
	public void runTrajectory(final String tag){
		if(trajectoryMap.containsKey(tag)) {
			drive.followTrajectory(trajectoryMap.get(tag));
		}else{
			drive.followTrajectorySequence(trajectorySequenceMap.get(tag));
		}
	}

	public TrajectoryBuilder generateBuilder(final Pose2d pose){
		return drive.trajectoryBuilder(pose);
	}
	public TrajectorySequenceBuilder generateSequenceBuilder(final Pose2d pose){
		return drive.trajectorySequenceBuilder(pose);
	}
}
