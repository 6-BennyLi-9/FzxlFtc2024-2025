package org.betastudio.ftc.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 一个非常好用的计时器
 */
public class Timer {
	/**
	 * 存储时间标签及其对应的时间值
	 */
	public final Map <String, Double> tags;

	/**
	 * 计时器的开始时间
	 */
	public double StartTime;

	/**
	 * 计时器的结束时间
	 */
	public double EndTime;

	/**
	 * 构造函数，初始化计时器的开始时间，并创建用于存储时间标签及其相关信息的Map
	 */
	public Timer() {
		StartTime = getCurrentTime();
		tags = new HashMap <>();
	}

	/**
	 * 获取当前时间，单位为毫秒
	 *
	 * @return 当前时间（毫秒）
	 */
	public static double getCurrentTime() {
		return System.nanoTime() / 1.0e6;
	}

	/**
	 * 重新定义{@code StartTime}为当前时间
	 */
	public void restart() {
		StartTime = getCurrentTime();
	}

	/**
	 * 定义{@code EndTime}为当前时间
	 */
	public void stop() {
		EndTime = getCurrentTime();
	}

	/**
	 * 获取从{@code StartTime}到{@code EndTime}的时间差（毫秒）
	 *
	 * @return 时间差（毫秒）
	 */
	public double getDeltaTime() {
		return EndTime - StartTime;
	}

	/**
	 * 定义{@code EndTime}为当前时间，并获取从{@code StartTime}到{@code EndTime}的时间差（毫秒）
	 *
	 * @return 时间差（毫秒）
	 */
	public double stopAndGetDeltaTime() {
		stop();
		return getDeltaTime();
	}

	/**
	 * 定义{@code EndTime}为当前时间，获取从{@code StartTime}到{@code EndTime}的时间差（毫秒），然后重新定义{@code StartTime}为当前时间
	 *
	 * @return 时间差（毫秒）
	 */
	public double restartAndGetDeltaTime() {
		final double res = stopAndGetDeltaTime();
		restart();
		return res;
	}

	/**
	 * 停止计时并重新开始计时
	 */
	public void stopAndRestart() {
		stop();
		restart();
	}

	/**
	 * 自动覆写如果存在相同的time tag，将新的时间值存入
	 *
	 * @param tag 时间标签
	 */
	public void pushTimeTag(final String tag) {
		if (tags.containsKey(tag)) {
			tags.replace(tag, getCurrentTime());
		} else {
			tags.put(tag, getCurrentTime());
		}
	}
}
