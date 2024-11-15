package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.client.TelemetryClient;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;

@Autonomous
public class BlueRight extends IntegralLinearOpMode {
	public SampleMecanumDrive drive;
	public TelemetryClient client;

	@Override
	public void initialize() {
		drive = new SampleMecanumDrive(hardwareMap);
		telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
		client = new TelemetryClient(telemetry);
	}

	@Override
	public void linear() {
	}
}