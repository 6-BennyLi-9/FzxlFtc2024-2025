package org.betastudio.ftc.thread;

import org.betastudio.ftc.thread.RunnableBuilder.RunnableSurrounder;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Deprecated
public class FtcThreadPool extends EasyThreadService implements FtcExecutorService {
	public class DefaultRunnableSurrounder implements RunnableSurrounder {
		@Override
		public Runnable surround(final Runnable runnable) {
			return () -> {
				final Thread thread = Thread.currentThread();
				threads.add(thread);
				runnable.run();
			};
		}
	}

	private static RunnableSurrounder defaultRunnableSurrounder = runnable -> runnable;

	private final Set <Thread> threads;

	public FtcThreadPool() {
		this(16, 32, 5000L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue <>(1024), Executors.defaultThreadFactory(), new ReleaseRejectionPolicy());
	}

	/**
	 * 会覆写默认的RunnableSurrounder，使得Runnable在执行前自动添加线程记录
	 */
	public FtcThreadPool(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final TimeUnit unit, final BlockingQueue <Runnable> workQueue, final ThreadFactory threadFactory, final RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
		threads = new TreeSet <>(Comparator.comparing(Thread::getName));
		defaultRunnableSurrounder = new DefaultRunnableSurrounder();
	}

	/**
	 * 自动构建Runnable，并添加线程记录
	 */
	@Override
	public void execute(final Runnable command) {
		super.execute(buildTask(command));
	}

	/**
	 * 自动构建Runnable，并添加线程记录
	 */
	@Override
	public Future <?> submit(final Runnable task) {
		return super.submit(buildTask(task));
	}

	/**
	 * 自动构建Runnable，并添加线程记录
	 */
	@Override
	public <T> Future <T> submit(final Runnable task, final T result) {
		return super.submit(buildTask(task), result);
	}

	@Override
	public Runnable buildTask(final Runnable task) {
		return new SampleRunnableBuilder(task).surround(new DefaultRunnableSurrounder()).build();
	}

	public static RunnableSurrounder getDefaultRunnableSurrounder() {
		return defaultRunnableSurrounder;
	}

	public static void setDefaultRunnableSurrounder(final RunnableSurrounder defaultRunnableSurrounder) {
		FtcThreadPool.defaultRunnableSurrounder = defaultRunnableSurrounder;
	}

	public static void overrideDefaultRunnableSurrounder(final RunnableSurrounder runnableSurrounder) {
		defaultRunnableSurrounder = runnable -> runnableSurrounder.surround(() -> defaultRunnableSurrounder.surround(runnable).run());
	}

	@Override
	public Set <Thread> getThreads() {
		return threads;
	}

	@Override
	public void submit(final String name, final Runnable task) {
		execute(new ThreadOperations.ThreadRenamer(name).surround(task));
	}

	@Override
	public void submit(final String prefix, final String suffix, final Runnable task) {
		execute(ThreadOperations.threadRenameWithSurround(prefix,suffix).surround(task));
	}

}
