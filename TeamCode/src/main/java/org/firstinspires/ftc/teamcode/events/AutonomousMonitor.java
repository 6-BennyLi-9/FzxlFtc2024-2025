package org.firstinspires.ftc.teamcode.events;

import org.betastudio.ftc.interfaces.ThreadAdditions;
import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.Local;
import org.firstinspires.ftc.teamcode.RunMode;

import java.util.concurrent.Callable;

/**
 * 这个类是一个自动监控线程，用于在自动模式下监控任务的状态。
 * 当任务中断或活动调用者返回false时，监控线程会将全局运行模式设置为终止。
 * AutonomousMonitor 类继承自 Thread 并实现了 ThreadAdditions 接口。
 * 它的主要功能是在自动模式下监控某个任务的活动状态，并提供关闭任务的方法。
 */
public final class AutonomousMonitor extends Thread implements ThreadAdditions {
	/**
	 * 一个 Callable 对象，用于检查任务是否处于活动状态。
	 * 如果该 Callable 返回 false，则表示任务不再处于活动状态。
	 */
	private final Callable <Boolean> activeCaller;

	/**
	 * 一个布尔标志，用于指示任务是否已被中断。
	 * 当调用 closeTask 方法时，该标志会被设置为 true。
	 */
	private       boolean            taskInterrupted;

	/**
	 * 构造函数，用于创建 AutonomousMonitor 实例。
	 *
	 * @param activeCaller 一个 Callable 对象，用于检查任务是否处于活动状态。
	 */
	public AutonomousMonitor(Callable <Boolean> activeCaller) {
		this.activeCaller = activeCaller;
	}

	/**
	 * 重写 Thread 类的 run 方法。
	 * 该方法会持续监控任务的状态，直到任务被中断或不再处于活动状态。
	 * 当上述条件之一满足时，它会将全局运行模式设置为终止。
	 */
	@Override
	public void run() {
		Local.waitForVal(() -> (taskInterrupted || ! activeCaller.call()), true);
		Global.runMode = RunMode.TERMINATE;
	}

	/**
	 * 实现 ThreadAdditions 接口的 closeTask 方法。
	 * 该方法用于中断当前监控的任务。
	 */
	@Override
	public void closeTask() {
		taskInterrupted = true;
	}
}
