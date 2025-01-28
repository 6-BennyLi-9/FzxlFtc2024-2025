package org.betastudio.ftc.ui.log;

import androidx.annotation.NonNull;

import org.betastudio.ftc.util.message.ExceptionMsg;
import org.betastudio.ftc.util.message.StringMsg;
import org.betastudio.ftc.util.message.TelemetryMsg;

public enum FtcLogTunnel {
	MAIN, DEBUG;
	private FtcLogFile log = new FtcLogFile();

	public static void clear() {
		for (final FtcLogTunnel tunnel : values()) {
			tunnel.log = new FtcLogFile();
		}
	}

	public static void saveFiles() {
		for (final FtcLogTunnel tunnel : values()) {
			tunnel.save();
		}
	}

	public static void saveAndClear() {
		saveFiles();
		clear();
	}

	public void report(final String s) {
		log.addElement(new FtcLogElement.ElementImpl(new StringMsg(s)));
	}

	public void report(@NonNull final Throwable e) {
		log.addElement(new FtcLogElement.ElementImpl(new ExceptionMsg(e)));
	}

	public void report(final FtcLogElement element) {
		log.addElement(element);
	}

	public TelemetryMsg call() {
		return log.call();
	}

	public void fresh() {
		log = new FtcLogFile();
	}

	public void save() {
		if (! log.getElements().isEmpty() && log.isUnsaved()) {
			FtcLogFilesBase.addFile(log.save());
		}
	}

	public void save(final String fileName) {
		if (! log.getElements().isEmpty() && log.isUnsaved()) {
			FtcLogFilesBase.addFile(log.save());
			log.setFileName(fileName);
		}
	}

	public FtcLogFile getLogFile() {
		return log;
	}
}
