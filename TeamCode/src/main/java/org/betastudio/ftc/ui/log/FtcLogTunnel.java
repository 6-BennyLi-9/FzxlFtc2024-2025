package org.betastudio.ftc.ui.log;

import androidx.annotation.NonNull;

import org.betastudio.ftc.time.Timestamp;
import org.betastudio.ftc.util.Labeler;
import org.betastudio.ftc.util.message.ExceptionMsg;
import org.betastudio.ftc.util.message.StringMsg;
import org.betastudio.ftc.util.message.TelemetryMsg;

public enum FtcLogTunnel {
	MAIN, @Deprecated DEBUG;
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
		report(new FtcLogElement.ElementImpl(new StringMsg(s)));
	}

	public void report(@NonNull final Throwable e) {
		report(new FtcLogElement.ElementImpl(new ExceptionMsg(e)));
	}

	public void report(final FtcLogElement element) {
		log.addElement(element);
	}

	public TelemetryMsg call() {
		return log.callMsg();
	}

	public void fresh() {
		log = new FtcLogFile();
	}

	public void save() {
		save(Labeler.generate().summonID(log) + new Timestamp());
	}

	public void save(final String fileName) {
		if (isSaveAble()) {
			FtcLogFilesBase.addFile(log.save());
			log.setFileName(fileName);
		}
	}

	public FtcLogFile getLogFile() {
		return log;
	}

	public boolean isSaveAble() {
		return ! log.getElements().isEmpty() && log.isUnsaved();
	}
}
