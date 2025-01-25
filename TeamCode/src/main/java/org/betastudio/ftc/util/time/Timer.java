package org.betastudio.ftc.util.time;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

/**
 * 一个非常好用的计时器
 */
public class Timer {
	/**
	 * 存储时间标签及其对应的时间值
	 */
	public final Map <String, Double> Tags;

	/**
	 * 存储时间标签及其对应的附加信息
	 */
	public final Map <String, Object> TagMeaning;

	/**
	 * 存储里程时间标签及其对应的时间值列表
	 */
	public final Map <String, Vector <Double>> MileageTags;

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
		Tags = new HashMap <>();
		TagMeaning = new HashMap <>();
		MileageTags = new HashMap <>();
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
		if (Tags.containsKey(tag)) {
			Tags.replace(tag, getCurrentTime());
		} else {
			Tags.put(tag, getCurrentTime());
		}
	}

	/**
	 * 自动覆写如果存在相同的time tag，将新的时间值存入，并关联附加信息
	 *
	 * @param tag       时间标签
	 * @param objection 附加信息
	 */
	public void pushObjectionTimeTag(final String tag, final Object objection) {
		pushTimeTag(tag);
		if (Tags.containsKey(tag)) {
			TagMeaning.replace(tag, objection);
		} else {
			TagMeaning.put(tag, objection);
		}
	}

	/**
	 * 获取指定time tag的时间值，如果未申明则返回0
	 *
	 * @param tag 时间标签
	 * @return 时间值（毫秒），未申明时返回0
	 */
	public double getTimeTag(final String tag) {
		final Double v = Tags.get(tag);
		return null == v ? 0 : v;
	}

	/**
	 * 获取指定time tag的附加信息，如果未申明则返回0
	 *
	 * @param tag 时间标签
	 * @return 附加信息，未申明时返回0
	 */
	public Object getTimeTagObjection(final String tag) {
		final Object v = TagMeaning.get(tag);
		return null == v ? 0 : v;
	}

	/**
	 * 添加一个里程时间标签，记录当前时间
	 *
	 * @param tag 时间标签
	 */
	public void pushMileageTimeTag(final String tag) {
		pushTimeTag(tag);
		if (MileageTags.containsKey(tag)) {
			Objects.requireNonNull(MileageTags.get(tag)).add(getCurrentTime());
		} else {
			final Vector <Double> cache = new Vector <>();
			cache.add(getCurrentTime());
			MileageTags.put(tag, cache);
		}
	}

	/**
	 * 获取指定里程时间标签的所有时间值记录
	 *
	 * @param tag 时间标签
	 * @return 时间值列表，未申明时返回null
	 */
	public Vector <Double> getMileageTimeTag(final String tag) {
		return MileageTags.get(tag);
	}
}
