package org.firstinspires.ftc.teamcode.events;

import static org.firstinspires.ftc.teamcode.Global.runMode;
import static org.firstinspires.ftc.teamcode.Global.threadManager;

import org.betastudio.ftc.util.ThreadEx;
import org.firstinspires.ftc.teamcode.Local;
import org.firstinspires.ftc.teamcode.RunMode;

/**
 * 定义一个名为 SystemMonitor 的最终类，该类继承自 Thread 并实现 ThreadEx 接口
 */
public final class SystemMonitor extends Thread implements ThreadEx {
	private boolean taskClosed; // 用于标记任务是否关闭的状态变量

	/**
	 * 重写 Thread 类的 run 方法，定义线程执行的任务
	 */
	@Override
	public void run() {
		// 当 runMode 不等于 RunMode.TERMINATE 且 taskClosed 为 false 时，线程持续运行
		while (RunMode.TERMINATE != runMode && ! taskClosed) {
			Local.sleep(5000); // 休眠 5000 毫秒（5 秒）
		}
		// 如果 taskClosed 为 false，表示是通过 runMode 为 RunMode.TERMINATE 退出的，此时中断所有线程
		if (! taskClosed) {
			threadManager.interruptAll();
		}
	}

	/**
	 * 实现 ThreadEx 接口的 closeTask 方法，用于关闭任务
	 */
	@Override
	public void closeTask() {
		taskClosed = true; // 将 taskClosed 置为 true，表示任务已关闭
	}
}
