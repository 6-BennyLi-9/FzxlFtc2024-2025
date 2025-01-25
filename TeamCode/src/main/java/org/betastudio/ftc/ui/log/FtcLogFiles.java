package org.betastudio.ftc.ui.log;

import java.util.Set;
import java.util.TreeSet;

public class FtcLogFiles {
	public static final Set <FtcLogDatabase> files;

	static {
		files = new TreeSet <>((o1, o2) -> (int) (o2.getSaveTime() - o1.getSaveTime()));
	}

	public static void addFile(final FtcLogDatabase file) {
		files.add(file);
	}

	public static Set <FtcLogDatabase> getFiles(){
		return files;
	}

}
