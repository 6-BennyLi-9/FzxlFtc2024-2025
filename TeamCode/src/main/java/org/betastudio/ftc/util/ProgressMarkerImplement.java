package org.betastudio.ftc.util;

import androidx.annotation.NonNull;

import org.betastudio.ftc.Interfaces.ProgressMarker;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

public class ProgressMarkerImplement implements ProgressMarker {
	private final AtomicLong total = new AtomicLong();
	private final AtomicLong done  = new AtomicLong();

	public ProgressMarkerImplement(final long total) {
		this(total, 0L);
	}

	public ProgressMarkerImplement(final long total, final long doneAlready) {
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

	/// 非特殊用途不推荐调用。
	public void setDone(final long done) {
		this.done.set(done);
	}

	@NonNull
	@Override
	public strictfp String toString() {
		return String.format(Locale.SIMPLIFIED_CHINESE, "%d/%d\n[%.5f%%]", done.get(), total.get(), getProgress() * 100);
	}
}
