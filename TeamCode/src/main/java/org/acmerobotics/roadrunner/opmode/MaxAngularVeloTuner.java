package org.acmerobotics.roadrunner.opmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.acmerobotics.roadrunner.SampleMecanumDrive;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Objects;

/**
 * This routine is designed to calculate the maximum angular velocity your bot can achieve under load.
 * <p>
 * Upon pressing start, your bot will turn at max power for RUNTIME seconds.
 * <p>
 * Further fine tuning of MAX_ANG_VEL may be desired.
 */
@Disabled
@Config
@Autonomous(group = "drive")
public class MaxAngularVeloTuner extends LinearOpMode {
	public static final double RUNTIME = 4.0;

	private double maxAngVelocity;

	@Override
	public void runOpMode() throws InterruptedException {
		final SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

		drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

		final Telemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

		telemetry.addLine("Your bot will turn at full speed for " + RUNTIME + " seconds.");
		telemetry.addLine("Please ensure you have enough space cleared.");
		telemetry.addLine("");
		telemetry.addLine("Press start when ready.");
		telemetry.update();

		waitForStart();

		telemetry.clearAll();
		telemetry.update();

		drive.setDrivePower(new Pose2d(0, 0, 1));
		final ElapsedTime timer = new ElapsedTime();

		while (! isStopRequested() && RUNTIME > timer.seconds()) {
			drive.updatePoseEstimate();

			final Pose2d poseVelo = Objects.requireNonNull(drive.getPoseVelocity(), "poseVelocity() must not be null. Ensure that the getWheelVelocities() method has been overridden in your localizer.");

			maxAngVelocity = Math.max(poseVelo.getHeading(), maxAngVelocity);
		}

		drive.setDrivePower(new Pose2d());

		telemetry.addData("Max Angular Velocity (rad)", maxAngVelocity);
		telemetry.addData("Max Angular Velocity (deg)", Math.toDegrees(maxAngVelocity));
		telemetry.addData("Max Recommended Angular Velocity (rad)", maxAngVelocity * 0.8);
		telemetry.addData("Max Recommended Angular Velocity (deg)", Math.toDegrees(maxAngVelocity * 0.8));
		telemetry.update();

		while (! isStopRequested()) idle();
	}
}
