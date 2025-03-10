package org.acmerobotics.roadrunner.util;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.WebHandlerManager;

import org.acmerobotics.roadrunner.DriveConstants;
import org.acmerobotics.roadrunner.SampleMecanumDrive;
import org.acmerobotics.roadrunner.StandardTrackingWheelLocalizer;
import org.firstinspires.ftc.ftccommon.external.WebHandlerRegistrar;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import fi.iki.elonen.NanoHTTPD;

public class LogFiles {
	private static final File ROOT = new File(AppUtil.ROOT_FOLDER + "/RoadRunner/logs/");

	public static        LogFile                             log          = new LogFile("uninitialized");
	private static final OpModeManagerNotifier.Notifications notifHandler = new OpModeManagerNotifier.Notifications() {
		@SuppressLint("SimpleDateFormat")
		final DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss_SSS");

		final ObjectWriter jsonWriter = new ObjectMapper(new JsonFactory()).writerWithDefaultPrettyPrinter();

		@Override
		public void onOpModePreInit(@NonNull final OpMode opMode) {
			log = new LogFile(opMode.getClass().getCanonicalName());

			// clean up old files
			final File[] fs = Objects.requireNonNull(ROOT.listFiles());
			Arrays.sort(fs, Comparator.comparingLong(File::lastModified));
			long totalSizeBytes = 0;
			for (final File f : fs) {
				totalSizeBytes += f.length();
			}

			int i = 0;
			while (i < fs.length && 32 * 1000 * 1000 <= totalSizeBytes) {
				totalSizeBytes -= fs[i].length();
				if (! fs[i].delete()) {
					RobotLog.setGlobalErrorMsg("Unable to delete file " + fs[i].getAbsolutePath());
				}
				++ i;
			}
		}

		@Override
		public void onOpModePreStart(final OpMode opMode) {
			log.nsStart = System.nanoTime();
		}

		@Override
		public void onOpModePostStop(final OpMode opMode) {
			log.nsStop = System.nanoTime();

			if (! (opMode instanceof OpModeManagerImpl.DefaultOpMode)) {
				//noinspection ResultOfMethodCallIgnored
				ROOT.mkdirs();

				final String filename = dateFormat.format(new Date(log.msInit)) + "__" + opMode.getClass().getSimpleName() + ".json";
				final File   file     = new File(ROOT, filename);
				try {
					jsonWriter.writeValue(file, log);
				} catch (final IOException e) {
					RobotLog.setGlobalErrorMsg(new RuntimeException(e), "Unable to write data to " + file.getAbsolutePath());
				}
			}
		}
	};

	public static void record(final Pose2d targetPose, final Pose2d pose, final double voltage, final List <Integer> lastDriveEncPositions, final List <Integer> lastDriveEncVels, final List <Integer> lastTrackingEncPositions, final List <Integer> lastTrackingEncVels) {
		final long nsTime = System.nanoTime();
		if (3 * 60 * 1_000_000_000L < nsTime - log.nsStart) {
			return;
		}

		log.nsTimes.add(nsTime);

		log.targetXs.add(targetPose.getX());
		log.targetYs.add(targetPose.getY());
		log.targetHeadings.add(targetPose.getHeading());

		log.xs.add(pose.getX());
		log.ys.add(pose.getY());
		log.headings.add(pose.getHeading());

		log.voltages.add(voltage);

		while (log.driveEncPositions.size() < lastDriveEncPositions.size()) {
			log.driveEncPositions.add(new ArrayList <>());
		}
		while (log.driveEncVels.size() < lastDriveEncVels.size()) {
			log.driveEncVels.add(new ArrayList <>());
		}
		while (log.trackingEncPositions.size() < lastTrackingEncPositions.size()) {
			log.trackingEncPositions.add(new ArrayList <>());
		}
		while (log.trackingEncVels.size() < lastTrackingEncVels.size()) {
			log.trackingEncVels.add(new ArrayList <>());
		}

		for (int i = 0 ; i < lastDriveEncPositions.size() ; i++) {
			log.driveEncPositions.get(i).add(lastDriveEncPositions.get(i));
		}
		for (int i = 0 ; i < lastDriveEncVels.size() ; i++) {
			log.driveEncVels.get(i).add(lastDriveEncVels.get(i));
		}
		for (int i = 0 ; i < lastTrackingEncPositions.size() ; i++) {
			log.trackingEncPositions.get(i).add(lastTrackingEncPositions.get(i));
		}
		for (int i = 0 ; i < lastTrackingEncVels.size() ; i++) {
			log.trackingEncVels.get(i).add(lastTrackingEncVels.get(i));
		}
	}

	/** @noinspection IOStreamConstructor*/
	@WebHandlerRegistrar
	public static void registerRoutes(@NonNull final WebHandlerManager manager) {
		//noinspection ResultOfMethodCallIgnored
		ROOT.mkdirs();

		// op mode manager only stores a weak reference, so we need to keep notifHandler alive ourselves
		// don't use @OnCreateEventLoop because it's unreliable
		OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity()).registerListener(notifHandler);

		manager.register("/logs", session -> {
			final StringBuilder sb = new StringBuilder();
			sb.append("<!doctype html><html><head><title>Logs</title></head><body><ul>");
			final File[] fs = Objects.requireNonNull(ROOT.listFiles());
			Arrays.sort(fs, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));
			for (final File f : fs) {
				sb.append("<li><a href=\"/logs/download?file=");
				sb.append(f.getName());
				sb.append("\" download=\"");
				sb.append(f.getName());
				sb.append("\">");
				sb.append(f.getName());
				sb.append("</a></li>");
			}
			sb.append("</ul></body></html>");
			return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, NanoHTTPD.MIME_HTML, sb.toString());
		});

		manager.register("/logs/download", session -> {
			final String[] pairs = session.getQueryParameterString().split("&");
			if (1 != pairs.length) {
				return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, NanoHTTPD.MIME_PLAINTEXT, "expected one query parameter, got " + pairs.length);
			}

			final String[] parts = pairs[0].split("=");
			if (! "file".equals(parts[0])) {
				return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, NanoHTTPD.MIME_PLAINTEXT, "expected file query parameter, got " + parts[0]);
			}

			final File f = new File(ROOT, parts[1]);
			if (! f.exists()) {
				return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "file " + f + " doesn't exist");
			}

			return NanoHTTPD.newChunkedResponse(NanoHTTPD.Response.Status.OK, "application/json", new FileInputStream(f));
		});
	}

	public static class LogFile {
		public String version = "quickstart1 v2";

		public final String opModeName;
		public final long   msInit = System.currentTimeMillis();
		public       long   nsInit = System.nanoTime();
		public long   nsStart, nsStop;

		public double  ticksPerRev     = DriveConstants.TICKS_PER_REV;
		public double  maxRpm          = DriveConstants.MAX_RPM;
		public boolean runUsingEncoder = DriveConstants.RUN_USING_ENCODER;
		public double  motorP          = DriveConstants.MOTOR_VELOCITY_PID.p;
		public double  motorI          = DriveConstants.MOTOR_VELOCITY_PID.i;
		public double  motorD          = DriveConstants.MOTOR_VELOCITY_PID.d;
		public double  motorF          = DriveConstants.MOTOR_VELOCITY_PID.f;
		public double  wheelRadius     = DriveConstants.WHEEL_RADIUS;
		public double  gearRatio       = DriveConstants.GEAR_RATIO;
		public double  trackWidth      = DriveConstants.TRACK_WIDTH;
		public double  kV              = DriveConstants.kV;
		public double  kA              = DriveConstants.kA;
		public double  kStatic         = DriveConstants.kStatic;
		public double  maxVel          = DriveConstants.MAX_VEL;
		public double  maxAccel        = DriveConstants.MAX_ACCEL;
		public double  maxAngVel       = DriveConstants.MAX_ANG_VEL;
		public double  maxAngAccel     = DriveConstants.MAX_ANG_ACCEL;

		public double mecTransP            = SampleMecanumDrive.TRANSLATIONAL_PID.kP;
		public double mecTransI            = SampleMecanumDrive.TRANSLATIONAL_PID.kI;
		public double mecTransD            = SampleMecanumDrive.TRANSLATIONAL_PID.kD;
		public double mecHeadingP          = SampleMecanumDrive.HEADING_PID.kP;
		public double mecHeadingI          = SampleMecanumDrive.HEADING_PID.kI;
		public double mecHeadingD          = SampleMecanumDrive.HEADING_PID.kD;
		public double mecLateralMultiplier = SampleMecanumDrive.LATERAL_MULTIPLIER;

		public double trackingTicksPerRev     = StandardTrackingWheelLocalizer.TICKS_PER_REV;
		public double trackingWheelRadius     = StandardTrackingWheelLocalizer.WHEEL_RADIUS;
		public double trackingGearRatio       = StandardTrackingWheelLocalizer.GEAR_RATIO;
		public double trackingLateralDistance = StandardTrackingWheelLocalizer.LATERAL_DISTANCE;
		public double trackingForwardOffset   = StandardTrackingWheelLocalizer.FORWARD_OFFSET;

		public RevHubOrientationOnRobot.LogoFacingDirection LOGO_FACING_DIR = DriveConstants.LOGO_FACING_DIR;
		public RevHubOrientationOnRobot.UsbFacingDirection  USB_FACING_DIR  = DriveConstants.USB_FACING_DIR;

		public final List <Long> nsTimes = new ArrayList <>();

		public final List <Double> targetXs = new ArrayList <>();
		public final List <Double> targetYs = new ArrayList <>();
		public final List <Double> targetHeadings = new ArrayList <>();

		public final List <Double> xs = new ArrayList <>();
		public final List <Double> ys = new ArrayList <>();
		public final List <Double> headings = new ArrayList <>();

		public final List <Double> voltages = new ArrayList <>();

		public final List <List <Integer>> driveEncPositions = new ArrayList <>();
		public final List <List <Integer>> driveEncVels      = new ArrayList <>();
		public final List <List <Integer>> trackingEncPositions = new ArrayList <>();
		public final List <List <Integer>> trackingEncVels      = new ArrayList <>();

		public LogFile(final String opModeName) {
			this.opModeName = opModeName;
		}
	}
}
