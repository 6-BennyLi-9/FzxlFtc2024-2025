package org.betastudio.ftc.util;

import org.betastudio.ftc.Interfaces;

public class ProgressMarker implements Interfaces.ProgressMarker {
	private final long total;
	private       long done;

	public ProgressMarker(long total) {
		this(total, 0L);
	}

	public ProgressMarker(long total, long doneAlready) {
		this.total = total;
		done = doneAlready;
	}

	@Override
	public long getTotal() {
		return total;
	}

	@Override
	public long getDone() {
		return done;
	}

	@Override
	public void tick() {
		++done;
	}
}
