package org.firstinspires.ftc.teamcode.events;

import static org.firstinspires.ftc.teamcode.Global.currentOpmode;

import android.util.Log;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;

import org.betastudio.ftc.util.entry.ThreadEx;
import org.firstinspires.ftc.cores.eventloop.IntegralAutonomous;
import org.firstinspires.ftc.cores.eventloop.IntegralOpMode;
import org.firstinspires.ftc.cores.eventloop.TerminateReason;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * 自定义线程异常处理器类，用于处理未捕获的线程异常。
 * 当线程中发生未捕获的异常时，该处理器会被调用以执行相应的处理逻辑。
 */
public class IntegralThreadExceptionHandler implements UncaughtExceptionHandler {
	/**
	 * 实现Thread.UncaughtExceptionHandler接口的uncaughtException方法。
	 * 当指定的线程中发生未捕获的异常时，该方法会被调用。
	 *
	 * @param t 发生异常的线程
	 * @param e 异常对象
	 */
	@Override
	public void uncaughtException(@NonNull final Thread t, @NonNull final Throwable e) {
		// 检查异常是否为OpModeManagerImpl.ForceStopException类型
		if (e instanceof OpModeManagerImpl.ForceStopException) {
			// 如果当前操作模式实现了ThreadAdditions接口，调用其closeTask方法
			if (currentOpmode instanceof ThreadEx) {
				((ThreadEx) currentOpmode).closeTask();
			} else {
				// 否则，直接终止当前操作模式
				currentOpmode.terminateOpModeNow();
			}
		} else {
			// 如果异常不是OpModeManagerImpl.ForceStopException类型，记录错误日志
			Log.e("Error", "OpMode Terminated By Exception", e);
			// 根据当前操作模式的类型，发送终止信号或直接终止操作模式
			if (currentOpmode instanceof IntegralAutonomous) {
				((IntegralOpMode) currentOpmode).sendTerminateSignal(TerminateReason.UNCAUGHT_EXCEPTION, (Exception) e);
			} else {
				currentOpmode.terminateOpModeNow();
			}
			// 启动任务关闭监控，监控发生异常的线程
			new TaskCloseMonitor(t).start();
		}
	}
}
