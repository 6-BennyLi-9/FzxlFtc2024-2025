package org.betastudio.ftc.util;

import androidx.annotation.NonNull;

import org.betastudio.ftc.specification.ThreadEx;
import org.betastudio.ftc.ui.log.FtcLogTunnel;
import org.firstinspires.ftc.teamcode.events.IntegralThreadExceptionHandler;
import org.firstinspires.ftc.teamcode.events.TaskCloseMonitor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 线程管理器类，用于管理多个线程，提供中断、添加线程等功能。
 */
public class ThreadManager {
	/**
	 * 用于存储线程的映射表，键为线程的标签，值为线程对象。
	 */
	private final Map<String, Thread> mem = new HashMap<>();
	/**
	 * 用于为未命名的线程生成唯一标签的工具。
	 */
	private final Labeler labeler = new Labeler();

	/**
	 * 构造函数，初始化线程管理器。
	 */
	public ThreadManager() {
	}

	/**
	 * 中断所有被管理的线程。
	 * 若线程实现了ThreadAdditions接口，则调用closeTask方法并启动TaskCloseMonitor监控线程关闭情况；
	 * 否则，直接中断线程。
	 * 最后清空存储线程的映射表。
	 */
	public void interruptAll() {
		for (final Map.Entry<String, Thread> entry : mem.entrySet()) {
			final Thread e = entry.getValue();
			if (e instanceof ThreadEx) {
				((ThreadEx) e).closeTask();
				new TaskCloseMonitor(e).start();
			} else {
				e.interrupt();
			}
		}
		mem.clear();
	}

	/**
	 * 根据标签中断指定的线程。
	 * 若映射表中存在该标签对应的线程，则中断该线程并从映射表中移除。
	 * 若不存在，则捕获NullPointerException异常但不做任何处理。
	 *
	 * @param tag 线程的标签
	 */
	public void interrupt(final String tag) {
		try {
			Objects.requireNonNull(mem.get(tag)).interrupt();
			mem.remove(tag);
		} catch (final NullPointerException exception) {
			FtcLogTunnel.MAIN.report(exception);
		}
	}

	/**
	 * 添加一个已经启动的线程到管理器中，并设置未捕获异常处理器。
	 *
	 * @param tag 线程的标签
	 * @param startedThread 已经启动的线程对象
	 */
	public void addStarted(final String tag, @NonNull final Thread startedThread) {
		startedThread.setUncaughtExceptionHandler(new IntegralThreadExceptionHandler());
		mem.put(tag, startedThread);
	}

	/**
	 * 添加一个未启动的线程到管理器中，首先启动该线程，然后调用addStarted方法进行管理。
	 * 会自动运行传入的线程。
	 *
	 * @param tag 线程的标签
	 * @param unstartedThread 未启动的线程对象
	 */
	public void add(final String tag, @NonNull final Thread unstartedThread) {
		unstartedThread.start();
		this.addStarted(tag, unstartedThread);
	}

	/**
	 * 添加一个未启动的线程到管理器中，使用Labeler为该线程生成一个唯一的标签。
	 * 会自动运行传入的线程。
	 *
	 * @param unstartedThread 未启动的线程对象
	 */
	public void add(@NonNull final Thread unstartedThread) {
		this.add(labeler.summonID(unstartedThread), unstartedThread);
	}

	/**
	 * 添加一个已经启动的线程到管理器中，使用Labeler为该线程生成一个唯一的标签。
	 *
	 * @param startedThread 已经启动的线程对象
	 */
	public void addStarted(final Thread startedThread) {
		this.addStarted(labeler.summonID(startedThread), startedThread);
	}

	/**
	 * 检查管理器中是否没有被管理的线程。
	 *
	 * @return 若管理器为空则返回true，否则返回false
	 */
	public boolean isEmpty() {
		return mem.isEmpty();
	}

	/**
	 * 获取存储线程的映射表。
	 *
	 * @return 线程的映射表
	 */
	public Map<String, Thread> getMem() {
		return mem;
	}
}
