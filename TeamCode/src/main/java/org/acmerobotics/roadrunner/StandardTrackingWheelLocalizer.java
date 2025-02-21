package org.acmerobotics.roadrunner;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.acmerobotics.roadrunner.util.Encoder;

import java.util.Arrays;
import java.util.List;

/*
 * Sample tracking wheel localizer implementation assuming the standard configuration:
 *
 *    /--------------\
 *    |     ____     |
 *    |     ----     |
 *    | ||        || |
 *    | ||        || |
 *    |              |
 *    |              |
 *    \--------------/
 *
 */
//@Config
public class StandardTrackingWheelLocalizer extends ThreeTrackingWheelLocalizer {
	public static final double TICKS_PER_REV = 4096;
	public static final double WHEEL_RADIUS  = 0.7480315; // in
	public static final double GEAR_RATIO    = 1; // output (wheel) speed / input (encoder) speed

	public static final double LATERAL_DISTANCE = 9.7; // in; distance between the left and right wheels
	public static final double FORWARD_OFFSET   = 0.395; // -0.25 in; offset of the lateral wheel

	public static final double X_MULTIPLIER = 1.017335420348886; // Multiplier in the X direction
	public static final double Y_MULTIPLIER = 1.017335420348886; // Multiplier in the Y direction

	private final Encoder leftEncoder;
	private final Encoder rightEncoder;
	private final Encoder frontEncoder;

	private final List <Integer> lastEncPositions;
	private final List <Integer> lastEncVels;

	public StandardTrackingWheelLocalizer(final HardwareMap hardwareMap, final List <Integer> lastTrackingEncPositions, final List <Integer> lastTrackingEncVels) {
		super(Arrays.asList(new Pose2d(0, LATERAL_DISTANCE / 2, 0), // left
				new Pose2d(0, - LATERAL_DISTANCE / 2, 0), // right
				new Pose2d(FORWARD_OFFSET, 0, Math.toRadians(90)) // front
		));

		lastEncPositions = lastTrackingEncPositions;
		lastEncVels = lastTrackingEncVels;

		leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "rightFront"));
		rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "leftRear"));
		frontEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "leftFront"));

		// TODO: reverse any encoders using Encoder.setDirection(Encoder.Direction.REVERSE)
		frontEncoder.setDirection(Encoder.Direction.REVERSE);
		rightEncoder.setDirection(Encoder.Direction.REVERSE);
	}

	public static double encoderTicksToInches(final double ticks) {
		return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
	}

	@NonNull
	@Override
	public List <Double> getWheelPositions() {
		final int leftPos  = leftEncoder.getCurrentPosition();
		final int rightPos = rightEncoder.getCurrentPosition();
		final int frontPos = frontEncoder.getCurrentPosition();

		lastEncPositions.clear();
		lastEncPositions.add(leftPos);
		lastEncPositions.add(rightPos);
		lastEncPositions.add(frontPos);

		return Arrays.asList(encoderTicksToInches(leftPos) * X_MULTIPLIER, encoderTicksToInches(rightPos) * X_MULTIPLIER, encoderTicksToInches(frontPos) * Y_MULTIPLIER);
	}

	@NonNull
	@Override
	public List <Double> getWheelVelocities() {
		final int leftVel  = (int) leftEncoder.getCorrectedVelocity();
		final int rightVel = (int) rightEncoder.getCorrectedVelocity();
		final int frontVel = (int) frontEncoder.getCorrectedVelocity();

		lastEncVels.clear();
		lastEncVels.add(leftVel);
		lastEncVels.add(rightVel);
		lastEncVels.add(frontVel);

		return Arrays.asList(encoderTicksToInches(leftVel) * X_MULTIPLIER, encoderTicksToInches(rightVel) * X_MULTIPLIER, encoderTicksToInches(frontVel) * Y_MULTIPLIER);
	}
}
