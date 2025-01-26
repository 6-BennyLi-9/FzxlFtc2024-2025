package org.betastudio.ftc.ui.log;

import androidx.annotation.NonNull;

import org.betastudio.ftc.util.message.StringMsg;
import org.betastudio.ftc.util.message.TelemetryMsg;

public enum FtcLogTunnel {
	MAIN,DEBUG;
	private FtcLogDatabase log = new FtcLogDatabase();

	public void report(final String s){
		log.addElement(new FtcLogElement.ElementImpl(new StringMsg(s)));
	}

	public void report(@NonNull final Throwable e){
		log.addElement(new FtcLogElement.ElementImpl(new StringMsg(e.getMessage())));
	}

	public void report(final FtcLogElement element){
		log.addElement(element);
	}

	public TelemetryMsg call(){
		return log.call();
	}

	public void fresh(){
		log=new FtcLogDatabase();
	}

	public static void clear(){
		for (final FtcLogTunnel tunnel : values()){
			tunnel.log=new FtcLogDatabase();
		}
	}

	public void save(){
		FtcLogFiles.addFile(log.save());
	}

	public static void saveFiles(){
		for (final FtcLogTunnel tunnel : values()){
			tunnel.save();
		}
	}

	public static void saveAndClear(){
		saveFiles();
		clear();
	}
}
