package org.betastudio.ftc.action.utils;

import org.betastudio.ftc.*;
import org.betastudio.ftc.action.*;
import org.betastudio.ftc.util.*;

import java.util.concurrent.atomic.*;

public final class RepetitionAction extends ActionImplementFactory implements Interfaces.ProgressedTask {
	private final ProgressMarker marker;

	public RepetitionAction(final Action repeatArgument, final long times) {
		final AtomicLong ptr = new AtomicLong();
		marker = new ProgressMarker(times);

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
