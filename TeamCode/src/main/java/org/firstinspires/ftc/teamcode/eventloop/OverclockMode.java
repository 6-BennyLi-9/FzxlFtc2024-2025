package org.firstinspires.ftc.teamcode.eventloop;

import org.betastudio.ftc.util.Timer;
import org.firstinspires.ftc.teamcode.Local;

import java.util.concurrent.atomic.AtomicLong;

public enum OverclockMode {
	SUPER_LINEAR, YIELD_AT_EACH, SLEEP_AT_EACH, MAX_FPS;
	public final Timer      timer              = new Timer();
	public final AtomicLong utilConfigureValue = new AtomicLong();

	public Runnable newLoop(final Runnable loopEntry) {
		switch (this) {
			case YIELD_AT_EACH:
				return () -> {
					Thread.yield();
					loopEntry.run();
				};
			case SLEEP_AT_EACH:
				return () -> {
					Local.sleep(utilConfigureValue.get());
					loopEntry.run();
				};
			case MAX_FPS:
				final long minTime = 1000 / utilConfigureValue.get();
				return () -> {
					Local.sleep(Math.max(0, minTime - (long) timer.stopAndGetDeltaTime()));
					loopEntry.run();
				};
			case SUPER_LINEAR:
			default:
				return loopEntry;
		}
	}
}
