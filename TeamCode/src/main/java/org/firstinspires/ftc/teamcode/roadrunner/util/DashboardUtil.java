package org.firstinspires.ftc.teamcode.roadrunner.util;

import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.path.Path;

import java.util.List;

/**
 * Set of helper functions for drawing Road Runner paths and trajectories on dashboard canvases.
 */
public enum DashboardUtil {
	;
	private static final double DEFAULT_RESOLUTION = 2.0; // distance units; presumed inches
	private static final double ROBOT_RADIUS       = 9; // in


	public static void drawPoseHistory(final Canvas canvas, final List <Pose2d> poseHistory) {
		final double[] xPoints = new double[poseHistory.size()];
		final double[] yPoints = new double[poseHistory.size()];
		for (int i = 0 ; i < poseHistory.size() ; i++) {
			final Pose2d pose = poseHistory.get(i);
			xPoints[i] = pose.getX();
			yPoints[i] = pose.getY();
		}
		canvas.strokePolyline(xPoints, yPoints);
	}

	public static void drawSampledPath(final Canvas canvas, final Path path, final double resolution) {
		final int      samples = (int) Math.ceil(path.length() / resolution);
		final double[] xPoints = new double[samples];
		final double[] yPoints = new double[samples];
		final double   dx      = path.length() / (samples - 1);
		for (int i = 0 ; i < samples ; i++) {
			final double displacement = i * dx;
			final Pose2d pose         = path.get(displacement);
			xPoints[i] = pose.getX();
			yPoints[i] = pose.getY();
		}
		canvas.strokePolyline(xPoints, yPoints);
	}

	public static void drawSampledPath(final Canvas canvas, final Path path) {
		drawSampledPath(canvas, path, DEFAULT_RESOLUTION);
	}

	public static void drawRobot(final Canvas canvas, final Pose2d pose) {
		canvas.strokeCircle(pose.getX(), pose.getY(), ROBOT_RADIUS);
		final Vector2d v  = pose.headingVec().times(ROBOT_RADIUS);
		final double   x1 = pose.getX() + v.getX() / 2;
		final double   y1 = pose.getY() + v.getY() / 2;
		final double   x2 = pose.getX() + v.getX();
		final double   y2 = pose.getY() + v.getY();
		canvas.strokeLine(x1, y1, x2, y2);
	}
}
