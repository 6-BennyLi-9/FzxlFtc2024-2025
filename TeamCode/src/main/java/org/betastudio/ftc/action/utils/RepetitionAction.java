package org.betastudio.ftc.action.utils;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.ActionImpl;

public final class RepetitionAction extends ActionImpl {
	private final long   times;
	private final Action argument;
	private       long   ptr;

	public RepetitionAction(final Action repeatArgument, final long times) {
		this.times = times;
		argument = repeatArgument;
		setAction(() -> {
			final boolean res = argument.activate();
			if (! res) return false;
			final boolean b = ptr < times;
			ptr++;
			return b;
		});
	}

	@NonNull
	@Override
	public String paramsString() {
		return "[" + ptr + "/" + times + "]" + argument.paramsString();
	}
}
