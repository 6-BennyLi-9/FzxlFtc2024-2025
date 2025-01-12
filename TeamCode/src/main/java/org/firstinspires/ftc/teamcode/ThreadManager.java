package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import org.betastudio.ftc.interfaces.ThreadAdditions;
import org.firstinspires.ftc.teamcode.events.IntegralThreadExceptionHandler;
import org.firstinspires.ftc.teamcode.events.TaskCloseMonitor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ThreadManager {
	private final Map <String, Thread> mem     = new HashMap <>();
	private final Labeler              labeler = new Labeler();

	public ThreadManager() {
	}

	public void interruptAll() {
		for (Map.Entry <String, Thread> entry : mem.entrySet()) {
			Thread e = entry.getValue();
			if (e instanceof ThreadAdditions) {
				((ThreadAdditions) e).closeTask();
				new TaskCloseMonitor(e).start();
			} else {
				e.interrupt();
			}
		}
		mem.clear();
	}

	public void interrupt(String tag) {
		try {
			Objects.requireNonNull(mem.get(tag)).interrupt();
			mem.remove(tag);
		}catch (NullPointerException ignored){}
	}

	public void addStarted(String tag, @NonNull Thread startedThread) {
		startedThread.setUncaughtExceptionHandler(new IntegralThreadExceptionHandler());
		mem.put(tag, startedThread);
	}

	/**
	 * 会自动运行
	 */
	public void add(String tag, @NonNull Thread unstartedThread) {
		unstartedThread.start();
		this.addStarted(tag, unstartedThread);
	}

	/**
	 * 会自动运行
	 */
	public void add(@NonNull Thread unstartedThread) {
		this.add(labeler.summonID(unstartedThread), unstartedThread);
	}

	public void addStarted(Thread startedThread) {
		this.addStarted(labeler.summonID(startedThread), startedThread);
	}

	public boolean isEmpty() {
		return mem.isEmpty();
	}

	public Map <String, Thread> getMem() {
		return mem;
	}
}
