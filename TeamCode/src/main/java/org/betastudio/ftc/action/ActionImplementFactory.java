package org.betastudio.ftc.action;

import static org.betastudio.ftc.Interfaces.Nameable;
import static org.betastudio.ftc.Interfaces.ThreadEx;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.utils.NullptrAction;
import org.betastudio.ftc.ui.log.FtcLogTunnel;

import java.util.concurrent.Callable;

/**
 * 子类只需调用 {@link #setAction(Callable)}并重写 {@link #paramsString()}即可
 */
public class ActionImplementFactory implements Action, ThreadEx, Nameable {
	private Callable <Boolean> action;
	private boolean            isStopRequested;
	private String             name = "*unnamed*";

	public ActionImplementFactory() {
		this(new NullptrAction());
	}

	public ActionImplementFactory(final Callable <Boolean> action) {
		this.action = action;
	}

	public ActionImplementFactory(@NonNull final Action action) {
		this.action = action::activate;
	}

	@Override
	public boolean activate() {
		if (isStopRequested) {
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
		isStopRequested = true;
	}

	public Callable <Boolean> getAction() {
		return action;
	}

	public void setAction(final Callable <Boolean> action) {
		this.action = action;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		 this.name = name;
	}
}
