package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.client.TelemetryClient;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;

import java.util.HashMap;
import java.util.Map;

@Autonomous
public class BlueLeft extends IntegralLinearOpMode {
	public SampleMecanumDrive drive;
	public TelemetryClient client;

	public Map <String, Trajectory> trajectoryMap=new HashMap <>();

	@Override
	public void initialize() {
		drive=new SampleMecanumDrive(hardwareMap);
		drive.setPoseEstimate(UtilPoses.BlueLeftStart.pose);
		telemetry=new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
		client=new TelemetryClient(telemetry);

		trajectoryMap.put("decant",drive.trajectoryBuilder(UtilPoses.BlueLeftStart.pose)
				.lineToSplineHeading(UtilPoses.BlueDecant.pose)
				.build());
		trajectoryMap.put("intake1",drive.trajectoryBuilder(UtilPoses.BlueDecant.pose)
				.lineToLinearHeading(UtilPoses.BlueLeftSample1.pose)
				.build());

		trajectoryMap.put("intake2",drive.trajectoryBuilder(UtilPoses.BlueLeftSample1.pose)
				.lineToLinearHeading(UtilPoses.BlueLeftSample2.pose)
				.build());

		trajectoryMap.put("intake3",drive.trajectoryBuilder(UtilPoses.BlueLeftSample2.pose)
				.lineToLinearHeading(UtilPoses.BlueLeftSample3.pose)
				.build());
	}

	@Override
	public void linear() {
		drive.followTrajectory(trajectoryMap.get("decant"));
		drive.followTrajectory(trajectoryMap.get("intake1"));
		drive.followTrajectory(trajectoryMap.get("intake2"));
		drive.followTrajectory(trajectoryMap.get("intake3"));
	}
}
