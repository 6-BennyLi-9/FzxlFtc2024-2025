package org.betastudio.ftc.ui.log;

import androidx.annotation.NonNull;

import org.betastudio.ftc.message.LogMessages;
import org.betastudio.ftc.message.TelemetryMsg;
import org.betastudio.ftc.util.Labeler;
import org.betastudio.ftc.util.Timestamp;

public enum FtcLogTunnel {
	MAIN, @Deprecated DEBUG;
	private FtcLogFile log = new FtcLogFile();

	private static void clear() {
		for (final FtcLogTunnel tunnel : values()) {
			tunnel.log = new FtcLogFile();
		}
	}

	private static void saveFiles() {
		for (final FtcLogTunnel tunnel : values()) {
			tunnel.save();
		}
	}

	public static void saveAndClear() {
		saveFiles();
		clear();
	}

	public void report(final String s) {
		report(new FtcLogElement.ElementImpl(new LogMessages.StringMsg(s)));
	}

	public void report(@NonNull final Throwable e) {
		report(new FtcLogElement.ElementImpl(new LogMessages.ExceptionMsg(e)));
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
		save(Labeler.gen().summon(log) + new Timestamp());
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
