package org.firstinspires.ftc.teamcode.roadrunner.util;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Utility functions for log files.
 */
public enum LoggingUtil {
	;
	public static final File ROAD_RUNNER_FOLDER = new File(AppUtil.ROOT_FOLDER + "/RoadRunner/");

	private static final long LOG_QUOTA = 25 * 1024 * 1024; // 25MB log quota for now

	private static void buildLogList(final List <File> logFiles, final File dir) {
		for (final File file : Objects.requireNonNull(dir.listFiles())) {
			if (file.isDirectory()) {
				buildLogList(logFiles, file);
			} else {
				logFiles.add(file);
			}
		}
	}

	private static void pruneLogsIfNecessary() {
		final List <File> logFiles = new ArrayList <>();
		buildLogList(logFiles, ROAD_RUNNER_FOLDER);
		logFiles.sort(Comparator.comparingLong(File::lastModified));

		long dirSize = 0;
		for (final File file : logFiles) {
			dirSize += file.length();
		}

		while (LOG_QUOTA < dirSize) {
			if (logFiles.isEmpty()) break;
			final File fileToRemove = logFiles.remove(0);
			dirSize -= fileToRemove.length();
			//noinspection ResultOfMethodCallIgnored
			fileToRemove.delete();
		}
	}

	/**
	 * Obtain a log file with the provided name
	 */
	public static File getLogFile(final String name) {
		//noinspection ResultOfMethodCallIgnored
		ROAD_RUNNER_FOLDER.mkdirs();

		pruneLogsIfNecessary();

		return new File(ROAD_RUNNER_FOLDER, name);
	}
}
