package org.acmerobotics.roadrunner.util;

import static com.qualcomm.hardware.rev.RevHubOrientationOnRobot.LogoFacingDirection;
import static com.qualcomm.hardware.rev.RevHubOrientationOnRobot.UsbFacingDirection;
import static com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier.*;
import static org.acmerobotics.roadrunner.DriveConstants.*;
import static org.acmerobotics.roadrunner.SampleMecanumDrive.HEADING_PID;
import static org.acmerobotics.roadrunner.SampleMecanumDrive.LATERAL_MULTIPLIER;
import static org.acmerobotics.roadrunner.SampleMecanumDrive.TRANSLATIONAL_PID;
import static org.acmerobotics.roadrunner.StandardTrackingWheelLocalizer.FORWARD_OFFSET;
import static org.acmerobotics.roadrunner.StandardTrackingWheelLocalizer.GEAR_RATIO;
import static org.acmerobotics.roadrunner.StandardTrackingWheelLocalizer.LATERAL_DISTANCE;
import static org.acmerobotics.roadrunner.StandardTrackingWheelLocalizer.TICKS_PER_REV;
import static org.acmerobotics.roadrunner.StandardTrackingWheelLocalizer.WHEEL_RADIUS;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.WebHandlerManager;

import org.acmerobotics.roadrunner.DriveConstants;
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

	public static        LogFile       log           = new LogFile("uninitialized");
	private static final Notifications notifyHandler = new Notifications() {
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

	/**
	 * @noinspection IOStreamConstructor
	 */
	@WebHandlerRegistrar
	public static void registerRoutes(@NonNull final WebHandlerManager manager) {
		//noinspection ResultOfMethodCallIgnored
		ROOT.mkdirs();

		// op mode manager only stores a weak reference, so we need to keep notifHandler alive ourselves
		// don't use @OnCreateEventLoop because it's unreliable
		OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity()).registerListener(notifyHandler);

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
		public final String                opModeName;
		public final long                  msInit                  = System.currentTimeMillis();
		public final List <Long>           nsTimes                 = new ArrayList <>();
		public final List <Double>         targetXs                = new ArrayList <>();
		public final List <Double>         targetYs                = new ArrayList <>();
		public final List <Double>         targetHeadings          = new ArrayList <>();
		public final List <Double>         xs                      = new ArrayList <>();
		public final List <Double>         ys                      = new ArrayList <>();
		public final List <Double>         headings                = new ArrayList <>();
		public final List <Double>         voltages                = new ArrayList <>();
		public final List <List <Integer>> driveEncPositions       = new ArrayList <>();
		public final List <List <Integer>> driveEncVels            = new ArrayList <>();
		public final List <List <Integer>> trackingEncPositions    = new ArrayList <>();
		public final List <List <Integer>> trackingEncVels         = new ArrayList <>();
		public       String                version                 = "quickstart1 v2";
		public       long                  nsInit                  = System.nanoTime();
		public       long                  nsStart;
		public       long                  nsStop;
		public       double                ticksPerRev             = DriveConstants.TICKS_PER_REV;
		public       double                maxRpm                  = MAX_RPM;
		public       boolean               runUsingEncoder         = RUN_USING_ENCODER;
		public       double                motorP                  = MOTOR_VELOCITY_PID.p;
		public       double                motorI                  = MOTOR_VELOCITY_PID.i;
		public       double                motorD                  = MOTOR_VELOCITY_PID.d;
		public       double                motorF                  = MOTOR_VELOCITY_PID.f;
		public       double                wheelRadius             = DriveConstants.WHEEL_RADIUS;
		public       double                gearRatio               = DriveConstants.GEAR_RATIO;
		public       double                trackWidth              = TRACK_WIDTH;
		public       double                kV                      = DriveConstants.kV;
		public       double                kA                      = DriveConstants.kA;
		public       double                kStatic                 = DriveConstants.kStatic;
		public       double                maxVel                  = MAX_VEL;
		public       double                maxAccel                = MAX_ACCEL;
		public       double                maxAngVel               = MAX_ANG_VEL;
		public       double                maxAngAccel             = MAX_ANG_ACCEL;
		public       double                mecTransP               = TRANSLATIONAL_PID.kP;
		public       double                mecTransI               = TRANSLATIONAL_PID.kI;
		public       double                mecTransD               = TRANSLATIONAL_PID.kD;
		public       double                mecHeadingP             = HEADING_PID.kP;
		public       double                mecHeadingI             = HEADING_PID.kI;
		public       double                mecHeadingD             = HEADING_PID.kD;
		public       double                mecLateralMultiplier    = LATERAL_MULTIPLIER;
		public       double                trackingTicksPerRev     = TICKS_PER_REV;
		public       double                trackingWheelRadius     = WHEEL_RADIUS;
		public       double                trackingGearRatio       = GEAR_RATIO;
		public       double                trackingLateralDistance = LATERAL_DISTANCE;
		public       double                trackingForwardOffset   = FORWARD_OFFSET;
		public       LogoFacingDirection   LOGO_FACING_DIR         = DriveConstants.LOGO_FACING_DIR;
		public       UsbFacingDirection    USB_FACING_DIR          = DriveConstants.USB_FACING_DIR;

		public LogFile(final String opModeName) {
			this.opModeName = opModeName;
		}
	}
}
