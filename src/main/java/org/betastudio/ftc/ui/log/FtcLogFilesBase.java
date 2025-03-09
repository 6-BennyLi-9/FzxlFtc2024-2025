package org.betastudio.ftc.ui.log;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class FtcLogFilesBase {
	private static final Set <FtcLogFile> files;

	static {
		files = new TreeSet <>(Comparator.comparingInt(o -> o.getSaveTime().toSecondsInt()));
	}

	public static void addFile(final FtcLogFile file) {
		files.add(file);
	}

	public static Set <FtcLogFile> getFiles() {
		return files;
	}
}
