package org.firstinspires.ftc.teamcode.events;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class FtcThreadFactory implements ThreadFactory {
	private static final AtomicInteger poolNumber   = new AtomicInteger(1);
	private final        ThreadGroup   group;
	private final        AtomicInteger threadNumber = new AtomicInteger(1);
	private final        String        namePrefix;

	public FtcThreadFactory() {
		SecurityManager s = System.getSecurityManager();
		group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
	}

	public Thread newThread(Runnable r) {
		Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
		if (t.isDaemon())
			t.setDaemon(false);
		if (t.getPriority() != Thread.NORM_PRIORITY)
			t.setPriority(Thread.NORM_PRIORITY);

		t.setUncaughtExceptionHandler(new IntegralThreadExceptionHandler());

		return t;
	}
}
