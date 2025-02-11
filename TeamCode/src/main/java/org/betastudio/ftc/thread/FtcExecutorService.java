package org.betastudio.ftc.thread;

import androidx.annotation.NonNull;

import org.betastudio.ftc.ui.log.FtcLogTunnel;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Deprecated
public interface FtcExecutorService extends ExecutorService {
	Runnable buildTask(Runnable task);

	Set <Thread> getThreads();

	void submit(String name, Runnable task);

	void submit(String prefix, String suffix, Runnable task);

	class ReleaseRejectionPolicy implements RejectedExecutionHandler {
		@Override
		public void rejectedExecution(@NonNull final Runnable r, @NonNull final ThreadPoolExecutor executor) {
			FtcLogTunnel.MAIN.report("Task " + r + " rejected from " + executor);
			r.run();
		}
	}
}
