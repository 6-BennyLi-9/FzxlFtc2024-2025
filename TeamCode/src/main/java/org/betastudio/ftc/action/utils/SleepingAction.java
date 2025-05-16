package org.betastudio.ftc.action.utils;

import static org.betastudio.ftc.Interfaces.ProgressedTask;

import org.betastudio.ftc.Interfaces.ProgressMarker;
import org.betastudio.ftc.action.ActionImplementFactory;
import org.betastudio.ftc.util.ProgressMarkerImplement;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class SleepingAction extends ActionImplementFactory implements ProgressedTask {
	private final ProgressMarkerImplement marker;

	public SleepingAction(final long sleepMilliseconds) {
		final AtomicLong    startTime   = new AtomicLong();
		final AtomicBoolean initialized = new AtomicBoolean();
		marker = new ProgressMarkerImplement(sleepMilliseconds);

		setAction(() -> {
			if (! initialized.get()) {
				startTime.set(System.currentTimeMillis());
				initialized.set(true);
			}
			long passed = System.currentTimeMillis() - startTime.get();
			marker.setDone(passed);
			return passed <= sleepMilliseconds;
		});

		setParams(() -> "t:" + sleepMilliseconds + "ms");

		setName(getName() + this.getClass().getSimpleName());
	}

	@Override
	public ProgressMarker getWorkerProgress() {
		return marker;
	}
}
