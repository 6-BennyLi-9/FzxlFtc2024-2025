package org.betastudio.ftc.thread;

import com.acmerobotics.dashboard.config.ValueProvider;

import org.betastudio.ftc.Interfaces;

import java.util.concurrent.Future;

public final class TaskFuture implements ValueProvider <String>, Interfaces.ValueProduction <Future <?>> {
	private final String     str;
	private final Future <?> future;

	public TaskFuture(final String str, final Future <?> future) {
		this.str = str;
		this.future = future;
	}

	@Override
	public String get() {
		return str;
	}

	/**
	 * @param value 不会干任何事
	 */
	@Override
	public void set(final String value) {
	}

	@Override
	public Future <?> getVal() {
		return future;
	}
}
