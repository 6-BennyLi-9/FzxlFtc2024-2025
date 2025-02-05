package org.betastudio.ftc.thread;

import androidx.annotation.NonNull;

public interface RunnableBuilder {
	RunnableBuilder surround(@NonNull RunnableSurrounder surrounder);

	RunnableBuilder appendPrefix(Runnable prefix);

	RunnableBuilder appendSuffix(Runnable suffix);

	Runnable build();

	interface RunnableSurrounder {
		Runnable surround(Runnable runnable);
	}
}
