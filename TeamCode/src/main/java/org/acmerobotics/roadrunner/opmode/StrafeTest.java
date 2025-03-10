package org.acmerobotics.roadrunner.opmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.acmerobotics.roadrunner.SampleMecanumDrive;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Local;

/*
 * This is a simple routine to test translational drive capabilities.
 */
@Disabled
@Config
@Autonomous(group = "drive")
public class StrafeTest extends LinearOpMode {
	public static final double DISTANCE = 60; // in

	@Override
	public void runOpMode() throws InterruptedException {
		final Telemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

		final SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

		final Trajectory trajectory = drive.trajectoryBuilder(new Pose2d()).strafeRight(DISTANCE).build();

		waitForStart();

		if (isStopRequested()) return;

		drive.followTrajectory(trajectory);

		final Pose2d poseEstimate = drive.getPoseEstimate();
		telemetry.addData("finalX", poseEstimate.getX());
		telemetry.addData("finalY", poseEstimate.getY());
		telemetry.addData("finalHeading", poseEstimate.getHeading());
		telemetry.update();

		while (! isStopRequested() && opModeIsActive()) Local.sleep(50);
	}
}
