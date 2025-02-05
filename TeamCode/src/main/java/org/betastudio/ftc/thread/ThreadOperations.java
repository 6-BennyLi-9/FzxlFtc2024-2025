package org.betastudio.ftc.thread;

import androidx.annotation.NonNull;

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
}
