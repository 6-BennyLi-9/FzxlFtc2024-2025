package org.betastudio.ftc.thread;

import androidx.annotation.NonNull;

public class SampleRunnableBuilder implements RunnableBuilder {
	private final Runnable runnable;

	public SampleRunnableBuilder(final Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public RunnableBuilder surround(@NonNull final RunnableSurrounder surrounder) {
		return new SampleRunnableBuilder(surrounder.surround(runnable));
	}

	@Override
	public RunnableBuilder appendPrefix(final Runnable prefix) {
		return surround(runnable -> () -> {
			prefix.run();
			runnable.run();
		});
	}

	@Override
	public RunnableBuilder appendSuffix(final Runnable suffix) {
		return surround(runnable -> () -> {
			runnable.run();
			suffix.run();
		});
	}

	@Override
	public Runnable build() {
		return runnable;
	}
}
