package org.betastudio.ftc.thread;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.utils.SleepingAction;
import org.betastudio.ftc.action.utils.StatementAction;
import org.betastudio.ftc.action.utils.ThreadedAction;
import org.betastudio.ftc.ui.client.InfinityLoopThread;
import org.jetbrains.annotations.Contract;

public class ThreadOperations {
	public static class ThreadRenamer implements RunnableBuilder.RunnableSurrounder {
		private final String name;

		public ThreadRenamer(final String name) {
			this.name = name;
		}

		@Override
		public Runnable surround(final Runnable runnable) {
			Thread.currentThread().setName(name);
			return runnable;
		}
	}

	public static class ThreadFormatRenamer implements RunnableBuilder.RunnableSurrounder {
		private final String format;

		/**
		 * @param format 需要预留一个 "%s" 作为占位符，将会被替换为线程名
		 */
		public ThreadFormatRenamer(final String format) {
			this.format = format;
		}

		@Override
		public Runnable surround(final Runnable runnable) {
			Thread.currentThread().setName(String.format(format, Thread.currentThread().getName()));
			return runnable;
		}
	}

	@NonNull
	@Contract(value = "_, _ -> new", pure = true)
	public static ThreadFormatRenamer threadRenameWithSurround(final String prefix, final String suffix) {
		return new ThreadFormatRenamer(prefix + "%s" + suffix);
	}

	@NonNull
	@Contract(value = "_, -> new", pure = true)
	public static Thread autoFrequencyCaller(final Runnable runnable) {
		return autoFrequencyCaller(runnable, 60L);
	}

	@NonNull
	@Contract(value = "_, _ -> new", pure = true)
	public static Thread autoFrequencyCaller(final Runnable runnable, final long fps) {
		return new InfinityLoopThread(new ThreadedAction(new SleepingAction(1000 / fps), new StatementAction(runnable)));
	}
}
