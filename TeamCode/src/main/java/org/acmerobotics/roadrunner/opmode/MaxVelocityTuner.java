package org.acmerobotics.roadrunner.opmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.acmerobotics.roadrunner.DriveConstants;
import org.acmerobotics.roadrunner.SampleMecanumDrive;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Objects;

/**
 * This routine is designed to calculate the maximum velocity your bot can achieve under load. It
 * will also calculate the effective kF value for your velocity PID.
 * <p>
 * Upon pressing start, your bot will run at max power for RUNTIME seconds.
 * <p>
 * Further fine tuning of kF may be desired.
 */
@Disabled
@Config
@Autonomous(group = "drive")
public class MaxVelocityTuner extends LinearOpMode {
	public static final double RUNTIME = 2.0;

	private double maxVelocity;

	@Override
	public void runOpMode() throws InterruptedException {
		final SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

		drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

		final VoltageSensor batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next();

		final Telemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

		telemetry.addLine("Your bot will go at full speed for " + RUNTIME + " seconds.");
		telemetry.addLine("Please ensure you have enough space cleared.");
		telemetry.addLine("");
		telemetry.addLine("Press start when ready.");
		telemetry.update();

		waitForStart();

		telemetry.clearAll();
		telemetry.update();

		drive.setDrivePower(new Pose2d(1, 0, 0));
		final ElapsedTime timer = new ElapsedTime();

		while (! isStopRequested() && RUNTIME > timer.seconds()) {
			drive.updatePoseEstimate();

			final Pose2d poseVelo = Objects.requireNonNull(drive.getPoseVelocity(), "poseVelocity() must not be null. Ensure that the getWheelVelocities() method has been overridden in your localizer.");

			maxVelocity = Math.max(poseVelo.vec().norm(), maxVelocity);
		}

		drive.setDrivePower(new Pose2d());

		final double effectiveKf = DriveConstants.getMotorVelocityF(veloInchesToTicks(maxVelocity));

		telemetry.addData("Max Velocity", maxVelocity);
		telemetry.addData("Max Recommended Velocity", maxVelocity * 0.8);
		telemetry.addData("Voltage Compensated kF", effectiveKf * batteryVoltageSensor.getVoltage() / 12);
		telemetry.update();

		while (! isStopRequested() && opModeIsActive()) idle();
	}

	private double veloInchesToTicks(final double inchesPerSec) {
		return inchesPerSec / (2 * Math.PI * DriveConstants.WHEEL_RADIUS) / DriveConstants.GEAR_RATIO * DriveConstants.TICKS_PER_REV;
	}
}
