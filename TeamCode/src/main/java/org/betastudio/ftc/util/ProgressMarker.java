package org.betastudio.ftc.util;

import androidx.annotation.NonNull;

import org.betastudio.ftc.Interfaces;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

public class ProgressMarker implements Interfaces.ProgressMarker {
	private final AtomicLong total = new AtomicLong();
	private final AtomicLong done  = new AtomicLong();

	public ProgressMarker(long total) {
		this(total, 0L);
	}

	public ProgressMarker(long total, long doneAlready) {
		this.total.set(total);
		done.set(doneAlready);
	}

	@Override
	public long getTotal() {
		return total.get();
	}

	@Override
	public long getDone() {
		return done.get();
	}

	@Override
	public void tick() {
		done.incrementAndGet();
	}

	@NonNull
	@Override
	public String toString() {
		return String.format(
				Locale.SIMPLIFIED_CHINESE,
				"%d/%d[%f]",
				done.get(),
				total.get(),
				getProgress());
	}
}
