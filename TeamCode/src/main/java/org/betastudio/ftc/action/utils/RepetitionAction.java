package org.betastudio.ftc.action.utils;

import androidx.annotation.NonNull;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.ActionImplementFactory;
import org.betastudio.ftc.util.ProgressMarker;

public final class RepetitionAction extends ActionImplementFactory implements Interfaces.ProgressedTask {
	private final ProgressMarker marker;
	private final long           times;
	private final Action         argument;
	private       long           ptr;

	public RepetitionAction(final Action repeatArgument, final long times) {
		this.times = times;
		argument = repeatArgument;
		marker = new ProgressMarker(times);
		setAction(() -> {
			final boolean res = argument.activate();
			if (! res)
				return false;
			final boolean b = ptr < times;
			ptr++;
			marker.tick();
			return b;
		});
	}

	@NonNull
	@Override
	public String paramsString() {
		return "[" + ptr + "/" + times + "]" + argument.paramsString();
	}

	@Override
	public Interfaces.ProgressMarker getWorkerProgress() {
		return marker;
	}
}
