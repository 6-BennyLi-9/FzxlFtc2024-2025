package org.firstinspires.ftc.teamcode.util;

import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.kinematics.Kinematics;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Various regression utilities.
 */
public enum RegressionUtil {
	;

	/**
     * Feedforward parameter estimates from the ramp regression and additional summary statistics
     */
    public static class RampResult {
        public final double kV, kStatic, rSquare;

        public RampResult(final double kV, final double kStatic, final double rSquare) {
            this.kV = kV;
            this.kStatic = kStatic;
            this.rSquare = rSquare;
        }
    }

    /**
     * Feedforward parameter estimates from the ramp regression and additional summary statistics
     */
    public static class AccelResult {
        public final double kA, rSquare;

        public AccelResult(final double kA, final double rSquare) {
            this.kA = kA;
            this.rSquare = rSquare;
        }
    }

    /**
     * Numerically compute dy/dx from the given x and y values. The returned list is padded to match
     * the length of the original sequences.
     *
     * @param x x-values
     * @param y y-values
     * @return derivative values
     */
    private static List<Double> numericalDerivative(final List<Double> x, final List<Double> y) {
        final List<Double> deriv = new ArrayList<>(x.size());
        for (int i = 1; i < x.size() - 1; i++) {
            deriv.add(
                    (y.get(i + 1) - y.get(i - 1)) /
                    (x.get(i + 1) - x.get(i - 1))
            );
        }
        // copy endpoints to pad output
        deriv.add(0, deriv.get(0));
        deriv.add(deriv.get(deriv.size() - 1));
        return deriv;
    }

    /**
     * Run regression to compute velocity and static feedforward from ramp test data.
     * <p>
     * Here's the general procedure for gathering the requisite data:
     *   1. Slowly ramp the motor power/voltage and record encoder values along the way.
     *   2. Run a linear regression on the encoder velocity vs. motor power plot to obtain a slope
     *      (kV) and an optional intercept (kStatic).
     *
     * @param timeSamples time samples
     * @param positionSamples position samples
     * @param powerSamples power samples
     * @param fitStatic fit kStatic
     * @param file log file
     */
    public static RampResult fitRampData(final List<Double> timeSamples, final List<Double> positionSamples,
                                         final List<Double> powerSamples, final boolean fitStatic,
                                         @Nullable final File file) {
        if (null != file) {
            try (final PrintWriter pw = new PrintWriter(file)) {
                pw.println("time,position,power");
                for (int i = 0; i < timeSamples.size(); i++) {
                    final double time = timeSamples.get(i);
                    final double pos = positionSamples.get(i);
                    final double power = powerSamples.get(i);
                    pw.println(time + "," + pos + "," + power);
                }
            } catch (final FileNotFoundException e) {
                // ignore
            }
        }

        final List<Double> velSamples = numericalDerivative(timeSamples, positionSamples);

        final SimpleRegression rampReg = new SimpleRegression(fitStatic);
        for (int i = 0; i < timeSamples.size(); i++) {
            final double vel = velSamples.get(i);
            final double power = powerSamples.get(i);

            rampReg.addData(vel, power);
        }

        return new RampResult(Math.abs(rampReg.getSlope()), Math.abs(rampReg.getIntercept()),
                              rampReg.getRSquare());
    }

    /**
     * Run regression to compute acceleration feedforward.
     *
     * @param timeSamples time samples
     * @param positionSamples position samples
     * @param powerSamples power samples
     * @param rampResult ramp result
     * @param file log file
     */
    public static AccelResult fitAccelData(final List<Double> timeSamples, final List<Double> positionSamples,
                                           final List<Double> powerSamples, final RampResult rampResult,
                                           @Nullable final File file) {
        if (null != file) {
            try (final PrintWriter pw = new PrintWriter(file)) {
                pw.println("time,position,power");
                for (int i = 0; i < timeSamples.size(); i++) {
                    final double time = timeSamples.get(i);
                    final double pos = positionSamples.get(i);
                    final double power = powerSamples.get(i);
                    pw.println(time + "," + pos + "," + power);
                }
            } catch (final FileNotFoundException e) {
                // ignore
            }
        }

        final List<Double> velSamples = numericalDerivative(timeSamples, positionSamples);
        final List<Double> accelSamples = numericalDerivative(timeSamples, velSamples);

        final SimpleRegression accelReg = new SimpleRegression(false);
        for (int i = 0; i < timeSamples.size(); i++) {
            final double vel = velSamples.get(i);
            final double accel = accelSamples.get(i);
            final double power = powerSamples.get(i);

            final double powerFromVel = Kinematics.calculateMotorFeedforward(
                    vel, 0.0, rampResult.kV, 0.0, rampResult.kStatic);
            final double powerFromAccel = power - powerFromVel;

            accelReg.addData(accel, powerFromAccel);
        }

        return new AccelResult(Math.abs(accelReg.getSlope()), accelReg.getRSquare());
    }
}
