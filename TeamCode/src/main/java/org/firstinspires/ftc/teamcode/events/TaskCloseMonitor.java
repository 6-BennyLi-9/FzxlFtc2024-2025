package org.firstinspires.ftc.teamcode.events;

import org.firstinspires.ftc.teamcode.Local;

/**
 * 任务关闭监控器类，用于监控指定线程的执行情况，并在超过设定的时间限制后终止该线程。
 * 该类继承自Thread类，因此可以作为一个独立的线程运行。
 */
public final class TaskCloseMonitor extends Thread {
	/**
	 * 需要监控的目标线程。
	 */
	public final Thread argument;

	/**
	 * 线程执行的最长时间限制，以毫秒为单位。
	 */
	public final long   timeLimit;

	/**
	 * 构造函数，创建一个新的TaskCloseMonitor实例，使用默认的时间限制5000毫秒。
	 *
	 * @param argument 需要监控的目标线程。
	 */
	public TaskCloseMonitor(Thread argument) {
		this(argument, 5000);
	}

	/**
	 * 构造函数，创建一个新的TaskCloseMonitor实例，并设置自定义的时间限制。
	 *
	 * @param argument  需要监控的目标线程。
	 * @param timeLimit 线程执行的最长时间限制，以毫秒为单位。
	 */
	public TaskCloseMonitor(Thread argument, long timeLimit) {
		this.argument = argument;
		this.timeLimit = timeLimit;
	}

	/**
	 * 重写Thread类的run方法，实现监控逻辑。
	 * 该方法会让当前线程休眠指定的时间限制，如果监控的目标线程在此期间没有结束，则会中断目标线程。
	 */
	@Override
	public void run() {
		Local.sleep(timeLimit);
		if (argument.getState() != State.TERMINATED) {
			argument.interrupt();
		}
	}
}
