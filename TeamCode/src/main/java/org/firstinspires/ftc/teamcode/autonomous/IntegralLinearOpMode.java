package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.client.TelemetryClient;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

import java.util.HashMap;
import java.util.Map;

public abstract class IntegralLinearOpMode extends LinearOpMode {
	public SampleMecanumDrive drive;
	public TelemetryClient client;

	private final Map<String, Trajectory> trajectoryMap=new HashMap<>();
	private final Map<String, TrajectorySequence> trajectorySequenceMap=new HashMap<>();

	@Override
	public final void runOpMode() throws InterruptedException {
		HardwareConstants.sync(hardwareMap, false);
		drive=new SampleMecanumDrive(hardwareMap);
		drive.setPoseEstimate(UtilPoses.BlueLeftStart.pose);
		telemetry=new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
		client=new TelemetryClient(telemetry);
		initialize();
		waitForStart();
		if(isStopRequested())return;
		linear();
	}

	public abstract void initialize();
	public abstract void linear();

	public void registerTrajectory(final String tag, final Trajectory argument){
		trajectoryMap.put(tag,argument);
	}
	public void registerTrajectory(final String tag, final TrajectorySequence argument){
		trajectorySequenceMap.put(tag,argument);
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
}
