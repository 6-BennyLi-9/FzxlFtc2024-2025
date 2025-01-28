package org.betastudio.ftc.action.utils;

import org.betastudio.ftc.action.Action;

/**
 * 类版本的 {@link RepeatAction} ，但需要提供要执行的 {@code Action} 块
 */
public final class RepetitionAction implements Action {
	private final long   times;
	private final Action argument;
	private       long   ptr;

	public RepetitionAction(final Action repeatArgument, final long times) {
		this.times = times;
		argument = repeatArgument;
	}

	@Override
	public boolean activate() {
		final boolean res = argument.activate();
		if (! res) return false;
		final boolean b = ptr < times;
		ptr++;
		return b;
	}

	@Override

	public String paramsString() {
		return "/" + times + "/" + argument.paramsString();
	}
}
