package org.betastudio.ftc.util;

/**
 * 一个非常好用的计数器
 */
public final class TickEncoder {
	/**
	 * 计数器的当前值
	 */
	private int ticked;

	/**
	 * 重置计数器的值为0
	 */
	public void tickRst() {
		ticked = 0;
	}

	/**
	 * 将计数器的值增加1
	 */
	public void tick() {
		++ ticked;
	}

	/**
	 * 将计数器的值对给定的模进行取模运算
	 *
	 * @param mod 模值，用于取模运算
	 */
	public void modTicked(final int mod) {
		ticked %= mod;
	}

	/**
	 * 先将计数器的值增加1，然后对给定的模进行取模运算
	 *
	 * @param mod 模值，用于取模运算
	 */
	public void tickAndMod(final int mod) {
		tick();
		modTicked(mod);
	}

	/**
	 * 获取计数器的当前值
	 *
	 * @return 计数器的当前值
	 */
	public int getTicked() {
		return ticked;
	}

	/**
	 * 设置计数器的当前值
	 *
	 * @param ticked 新的计数值
	 */
	public void setTicked(final int ticked) {
		this.ticked = ticked;
	}
}
