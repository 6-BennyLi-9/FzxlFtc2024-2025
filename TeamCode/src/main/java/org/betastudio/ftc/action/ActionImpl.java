package org.betastudio.ftc.action;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.utils.NullptrAction;
import org.betastudio.ftc.ui.log.FtcLogTunnel;
import org.betastudio.ftc.util.entry.ThreadEx;

import java.util.concurrent.Callable;

public class ActionImpl extends Thread implements Action, ThreadEx {
	private Callable <Boolean> action;
	private boolean isStopRequested;

	public ActionImpl() {
		this(new NullptrAction());
	}

	public ActionImpl(final Callable<Boolean> action) {
		this.action = action;
	}

	public ActionImpl(@NonNull final Action action){
		this.action = action::activate;
	}

	@Override
	public boolean activate() {
		if(isStopRequested){
			return false;
		}
		try {
			return action.call();
		} catch (final Exception e) {
			FtcLogTunnel.MAIN.report(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void closeTask() {
		isStopRequested=true;
	}

	public void setAction(final Callable <Boolean> action) {
		this.action = action;
	}

	public Callable <Boolean> getAction() {
		return action;
	}
}
