package org.firstinspires.ftc.teamcode.autonomous.utils;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.client.TelemetryClient;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.util.HardwareConstants;
import org.firstinspires.ftc.teamcode.util.Timer;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused","UnusedReturnValue"})
public abstract class IntegralLinearOp extends LinearOpMode {
	public SampleMecanumDrive drive;
	public TelemetryClient    client;
	public Util               utils;
	public Timer              timer;

	private final Map<String, Trajectory> trajectoryMap=new HashMap<>();
	private final Map<String, TrajectorySequence> trajectorySequenceMap=new HashMap<>();

	@Override
	public final void runOpMode() throws InterruptedException {
		try {
			HardwareConstants.sync(hardwareMap, false);
			drive=new SampleMecanumDrive(hardwareMap);
			telemetry=new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
			client=new TelemetryClient(telemetry);
			utils=new Util();
			timer=new Timer();
			initialize();

			TelemetryClient.getInstance().addLine(">>>ROBOT READY!");

			waitForStart();

			TelemetryClient.getInstance().deleteLine(">>>ROBOT READY!");

			if(!opModeIsActive())return;
			timer.restart();
			Thread linear=new Thread(this::linear);
			linear.start();
			while (opModeIsActive()&&!linear.isInterrupted()){
				sleep(10);
			}
			linear.interrupt();
		}catch (Throwable ignore){}
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

	public Trajectory angleCalibration(final double angle){
		return drive.trajectoryBuilder(drive.getPoseEstimate())
				.lineToLinearHeading(drive.getPoseEstimate().minus(new Pose2d(
						drive.getPoseEstimate().getX(),
						drive.getPoseEstimate().getY(),
						angle
				))).build();
	}
}
