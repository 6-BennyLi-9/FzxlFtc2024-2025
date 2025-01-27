package org.betastudio.ftc.ui.log;

import java.util.Set;
import java.util.TreeSet;

public class FtcLogFilesBase {
	private static final Set <FtcLogFile> files;

	static {
		files = new TreeSet <>((o1, o2) -> (int) (o1.getSaveTime() - o2.getSaveTime()));
	}

	public static void addFile(final FtcLogFile file) {
		files.add(file);
	}

	public static Set <FtcLogFile> getFiles(){
		return files;
	}
}
