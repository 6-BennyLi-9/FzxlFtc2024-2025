package org.betastudio.application.action.utils;

import org.betastudio.application.action.Action;

/**
 * 自动循环的 {@code Action} 块，重写了部分方法，但是请注意：不要二次重写已经重写了的方法
 *
 * @implNote 不要重写已经重写了的方法，否则你还不如自己写一个新的接口或者抽象类
 */
public interface RepeatAction extends Action {
	/**
	 * 用于计数，为 package-private ，也就代表着该代码不会对其他作用域有任何实机用处
	 */
	class Ticker {
		private long tickedTicks;

		Ticker() {
		}//package-private

		public void tick() {
			++ tickedTicks;
		}

		public long getTickedTicks() {
			return tickedTicks;
		}
	}

	Ticker ticker = new Ticker();

	long getRepeatTime();

	String integralParamsString();

	/**
	 * @return 与 {@link  Action#run()} 类似，返回 false 时退出循环
	 */
	boolean loop();

	/**
	 * 不要重写该方法，否则你还不如自己写一个新的接口或者抽象类
	 *
	 * @implNote 不要重写该方法
	 */
	@Override
	default boolean run() {
		ticker.tick();
		return loop() && ticker.getTickedTicks() <= getRepeatTime();
	}

	/**
	 * 不要重写该方法，否则你还不如自己写一个新的接口或者抽象类
	 *
	 * @implNote 不要重写该方法
	 */
	@Override
	default String paramsString() {
		return "|" + getRepeatTime() + "|" + integralParamsString();
	}
}
