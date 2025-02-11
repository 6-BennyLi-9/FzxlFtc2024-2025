package org.betastudio.ftc.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class EasyThreadService extends ThreadPoolExecutor {
	public EasyThreadService() {
		this(16, 32, 5000L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue <>(1024), Executors.defaultThreadFactory(), new FtcExecutorService.ReleaseRejectionPolicy());
	}

	/**
	 * 会覆写默认的RunnableSurrounder，使得Runnable在执行前自动添加线程记录
	 */
	public EasyThreadService(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final TimeUnit unit, final BlockingQueue <Runnable> workQueue, final ThreadFactory threadFactory, final RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
	}
}
