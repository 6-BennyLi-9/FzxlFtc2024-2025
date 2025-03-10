package org.acmerobotics.roadrunner;

import static org.acmerobotics.roadrunner.DriveConstants.MAX_ACCEL;
import static org.acmerobotics.roadrunner.DriveConstants.MAX_ANG_ACCEL;
import static org.acmerobotics.roadrunner.DriveConstants.MAX_ANG_VEL;
import static org.acmerobotics.roadrunner.DriveConstants.MAX_VEL;
import static org.acmerobotics.roadrunner.DriveConstants.MOTOR_VELOCITY_PID;
import static org.acmerobotics.roadrunner.DriveConstants.RUN_USING_ENCODER;
import static org.acmerobotics.roadrunner.DriveConstants.TRACK_WIDTH;
import static org.acmerobotics.roadrunner.DriveConstants.encoderTicksToInches;
import static org.acmerobotics.roadrunner.DriveConstants.kA;
import static org.acmerobotics.roadrunner.DriveConstants.kStatic;
import static org.acmerobotics.roadrunner.DriveConstants.kV;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.drive.DriveSignal;
import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.acmerobotics.roadrunner.followers.HolonomicPIDVAFollower;
import com.acmerobotics.roadrunner.followers.TrajectoryFollower;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.acmerobotics.roadrunner.trajectorysequence.TrajectorySequence;
import org.acmerobotics.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import org.acmerobotics.roadrunner.trajectorysequence.TrajectorySequenceRunner;
import org.acmerobotics.roadrunner.util.LynxModuleUtil;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Simple mecanum drive hardware implementation for REV hardware.
 */
@Config
public class SampleMecanumDrive extends MecanumDrive {
	private static final TrajectoryVelocityConstraint     VEL_CONSTRAINT     = getVelocityConstraint(MAX_VEL, MAX_ANG_VEL, TRACK_WIDTH);
	private static final TrajectoryAccelerationConstraint ACCEL_CONSTRAINT  = getAccelerationConstraint(MAX_ACCEL);
	public static final  PIDCoefficients                  TRANSLATIONAL_PID = new PIDCoefficients(8, 0, 0);
	public static final  PIDCoefficients                  HEADING_PID       = new PIDCoefficients(8, 0, 0);
	public static final double          LATERAL_MULTIPLIER = 1;
	public static final double          VX_WEIGHT          = 1;
	public static final double VY_WEIGHT          = 1;
	public static final double OMEGA_WEIGHT = 1;
	private final       TrajectorySequenceRunner trajectorySequenceRunner;

	private final DcMotorEx        leftFront;
	private final DcMotorEx        leftRear;
	private final DcMotorEx        rightRear;
	private final DcMotorEx        rightFront;
	private final List <DcMotorEx> motors;

	private final VoltageSensor batteryVoltageSensor;

	private final List <Integer> lastEncPositions = new ArrayList <>();
	private final List <Integer> lastEncVels      = new ArrayList <>();

	public SampleMecanumDrive(final HardwareMap hardwareMap) {
		super(kV, kA, kStatic, TRACK_WIDTH, TRACK_WIDTH, LATERAL_MULTIPLIER);

		final TrajectoryFollower follower = new HolonomicPIDVAFollower(TRANSLATIONAL_PID, TRANSLATIONAL_PID, HEADING_PID, new Pose2d(0.5, 0.5, Math.toRadians(5.0)), 0.5);

		LynxModuleUtil.ensureMinimumFirmwareVersion(hardwareMap);

		batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next();

		for (final LynxModule module : hardwareMap.getAll(LynxModule.class)) {
			module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
		}

		//DONE_TODO: adjust the names of the following hardware devices to match your configuration

		leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
		leftRear = hardwareMap.get(DcMotorEx.class, "leftRear");
		rightRear = hardwareMap.get(DcMotorEx.class, "rightRear");
		rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");

		motors = Arrays.asList(leftFront, leftRear, rightRear, rightFront);

		for (final DcMotorEx motor : motors) {
			final MotorConfigurationType motorConfigurationType = motor.getMotorType().clone();
			motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
			motor.setMotorType(motorConfigurationType);
		}

		if (RUN_USING_ENCODER) {
			setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		}

		setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		if (RUN_USING_ENCODER && null != MOTOR_VELOCITY_PID) {
			setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, MOTOR_VELOCITY_PID);
		}

		// DONE_TODO: reverse any motors using DcMotor.setDirection()
		leftFront.setDirection(DcMotorSimple.Direction.FORWARD);   //F
		leftRear.setDirection(DcMotorSimple.Direction.FORWARD);    //F
		rightFront.setDirection(DcMotorSimple.Direction.REVERSE);  //R
		rightRear.setDirection(DcMotorSimple.Direction.REVERSE);   //R

		final List <Integer> lastTrackingEncPositions = new ArrayList <>();
		final List <Integer> lastTrackingEncVels      = new ArrayList <>();

		// DONE_TODO: if desired, use setLocalizer() to change the localization method
		setLocalizer(new StandardTrackingWheelLocalizer(hardwareMap, lastTrackingEncPositions, lastTrackingEncVels));

		trajectorySequenceRunner = new TrajectorySequenceRunner(follower, HEADING_PID, batteryVoltageSensor, lastEncPositions, lastEncVels, lastTrackingEncPositions, lastTrackingEncVels);
	}

	@NonNull
	@Contract("_, _, _ -> new")
	public static TrajectoryVelocityConstraint getVelocityConstraint(final double maxVel, final double maxAngularVel, final double trackWidth) {
		return new MinVelocityConstraint(Arrays.asList(new AngularVelocityConstraint(maxAngularVel), new MecanumVelocityConstraint(maxVel, trackWidth)));
	}

	@NonNull
	@Contract(value = "_ -> new", pure = true)
	public static TrajectoryAccelerationConstraint getAccelerationConstraint(final double maxAccel) {
		return new ProfileAccelerationConstraint(maxAccel);
	}

	public TrajectoryBuilder trajectoryBuilder(final Pose2d startPose) {
		return new TrajectoryBuilder(startPose, VEL_CONSTRAINT, ACCEL_CONSTRAINT);
	}

	public TrajectoryBuilder trajectoryBuilder(final Pose2d startPose, final boolean reversed) {
		return new TrajectoryBuilder(startPose, reversed, VEL_CONSTRAINT, ACCEL_CONSTRAINT);
	}

	public TrajectoryBuilder trajectoryBuilder(final Pose2d startPose, final double startHeading) {
		return new TrajectoryBuilder(startPose, startHeading, VEL_CONSTRAINT, ACCEL_CONSTRAINT);
	}

	public TrajectorySequenceBuilder trajectorySequenceBuilder(final Pose2d startPose) {
		return new TrajectorySequenceBuilder(startPose, VEL_CONSTRAINT, ACCEL_CONSTRAINT, MAX_ANG_VEL, MAX_ANG_ACCEL);
	}

	public void turnAsync(final double angle) {
		trajectorySequenceRunner.followTrajectorySequenceAsync(trajectorySequenceBuilder(getPoseEstimate()).turn(angle).build());
	}

	public void turn(final double angle) {
		turnAsync(angle);
		waitForIdle();
	}

	public void followTrajectoryAsync(@NonNull final Trajectory trajectory) {
		trajectorySequenceRunner.followTrajectorySequenceAsync(trajectorySequenceBuilder(trajectory.start()).addTrajectory(trajectory).build());
	}

	public void followTrajectory(final Trajectory trajectory) {
		followTrajectoryAsync(trajectory);
		waitForIdle();
	}

	public void followTrajectorySequenceAsync(final TrajectorySequence trajectorySequence) {
		trajectorySequenceRunner.followTrajectorySequenceAsync(trajectorySequence);
	}

	public void followTrajectorySequence(final TrajectorySequence trajectorySequence) {
		followTrajectorySequenceAsync(trajectorySequence);
		waitForIdle();
	}

	public Pose2d getLastError() {
		return trajectorySequenceRunner.getLastPoseError();
	}

	public void update() {
		updatePoseEstimate();
		final DriveSignal signal = trajectorySequenceRunner.update(getPoseEstimate(), getPoseVelocity());
		if (null != signal) setDriveSignal(signal);
	}

	public void waitForIdle() {
		while (! Thread.currentThread().isInterrupted() && isBusy()) update();
	}

	public boolean isBusy() {
		return trajectorySequenceRunner.isBusy();
	}

	public void setMode(final DcMotor.RunMode runMode) {
		for (final DcMotorEx motor : motors) {
			motor.setMode(runMode);
		}
	}

	public void setZeroPowerBehavior(final DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
		for (final DcMotorEx motor : motors) {
			motor.setZeroPowerBehavior(zeroPowerBehavior);
		}
	}

	public void setPIDFCoefficients(final DcMotor.RunMode runMode, @NonNull final PIDFCoefficients coefficients) {
		final PIDFCoefficients compensatedCoefficients = new PIDFCoefficients(coefficients.p, coefficients.i, coefficients.d, coefficients.f * 12 / batteryVoltageSensor.getVoltage());

		for (final DcMotorEx motor : motors) {
			motor.setPIDFCoefficients(runMode, compensatedCoefficients);
		}
	}

	public void setWeightedDrivePower(@NonNull final Pose2d drivePower) {
		Pose2d vel = drivePower;

		if (1 < Math.abs(drivePower.getX()) + Math.abs(drivePower.getY()) + Math.abs(drivePower.getHeading())) {
			// re-normalize the powers according to the weights
			final double denom = VX_WEIGHT * Math.abs(drivePower.getX()) + VY_WEIGHT * Math.abs(drivePower.getY()) + OMEGA_WEIGHT * Math.abs(drivePower.getHeading());

			vel = new Pose2d(VX_WEIGHT * drivePower.getX(), VY_WEIGHT * drivePower.getY(), OMEGA_WEIGHT * drivePower.getHeading()).div(denom);
		}

		setDrivePower(vel);
	}

	@NonNull
	@Override
	public List <Double> getWheelPositions() {
		lastEncPositions.clear();

		final List <Double> wheelPositions = new ArrayList <>();
		for (final DcMotorEx motor : motors) {
			final int position = motor.getCurrentPosition();
			lastEncPositions.add(position);
			wheelPositions.add(encoderTicksToInches(position));
		}
		return wheelPositions;
	}

	@Override
	public List <Double> getWheelVelocities() {
		lastEncVels.clear();

		final List <Double> wheelVelocities = new ArrayList <>();
		for (final DcMotorEx motor : motors) {
			final int vel = (int) motor.getVelocity();
			lastEncVels.add(vel);
			wheelVelocities.add(encoderTicksToInches(vel));
		}
		return wheelVelocities;
	}

	@Override
	public void setMotorPowers(final double v, final double v1, final double v2, final double v3) {
		leftFront.setPower(v);
		leftRear.setPower(v1);
		rightRear.setPower(v2);
		rightFront.setPower(v3);
	}

	@Override
	public double getRawExternalHeading() {
		return 0;
	}

	@Override
	public Double getExternalHeadingVelocity() {
		return 0.0;
	}
}
