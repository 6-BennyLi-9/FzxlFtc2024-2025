package org.betastudio.application.action.utils;

import org.betastudio.application.action.Action;
import org.jetbrains.annotations.NotNull;

/**
 * 类版本的 {@link RepeatAction} ，但需要提供要执行的 {@code Action} 块
 */
public final class RepetitionAction implements Action {
	private final long   times;
	private       long   ptr;
	private final Action argument;

	public RepetitionAction(final Action repeatArgument, final long times) {
		this.times = times;
		argument = repeatArgument;
	}

	@Override
	public boolean run() {
		final boolean res = argument.run();
		if (! res) return false;
		final boolean b = ptr < times;
		ptr++;
		return b;
	}

	@Override
	@NotNull
	public String paramsString() {
		return "/" + times + "/" + argument.paramsString();
	}
}
