package org.betastudio.ftc.action.utils;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.ActionImplementFactory;
import org.betastudio.ftc.util.ProgressMarkerImplement;

import java.util.concurrent.atomic.AtomicLong;

public final class RepetitionAction extends ActionImplementFactory implements Interfaces.ProgressedTask {
	private final ProgressMarkerImplement marker;

	public RepetitionAction(final Action repeatArgument, final long times) {
		final AtomicLong ptr = new AtomicLong();
		marker = new ProgressMarkerImplement(times);

		setAction(() -> {
			final boolean res = repeatArgument.activate();
			if (! res)
				return false;
			final boolean b = ptr.get() < times;
			ptr.getAndIncrement();
			marker.tick();
			return b;
		});

		setParams(() -> "[" + ptr + "/" + times + "]" + repeatArgument.paramsString());

		setName(getName() + this.getClass().getSimpleName());
	}

	@Override
	public Interfaces.ProgressMarker getWorkerProgress() {
		return marker;
	}
}
