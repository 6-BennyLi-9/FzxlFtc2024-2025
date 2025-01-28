package org.betastudio.ftc.util;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.ButtonConfig;

/**
 * 用于控制按键布尔状态，更加轻量化
 */
public final class ButtonProcessor {
	/**
	 * 按键配置实例，定义了按键的响应方式
	 */
	public final ButtonConfig config;
	/**
	 * 智能计数器，用于记录按键状态的变化
	 */
	public final TickEncoder  ticker;
	/**
	 * 上一个按键状态
	 */
	private      boolean      lst,
	/**
	 * 当前按键状态
	 */
	now;

	/**
	 * 构造函数，初始化按键处理器
	 *
	 * @param config 按键配置，定义按键如何响应
	 */
	public ButtonProcessor(final ButtonConfig config) {
		this.config = config;
		ticker = new TickEncoder();
	}

	/**
	 * 同步输入的状态到处理器内部
	 *
	 * @param input 当前按键的实际状态
	 */
	public void sync(final boolean input) {
		switch (config) {
			case WHILE_PRESSING:
				now = input;
				break;
			case SINGLE_WHEN_PRESSED:
			default:
				lst = now;
				now = input;
				break;
		}
	}

	/**
	 * 获取按键是否启用的状态
	 * 根据配置返回不同的启用状态
	 *
	 * @return 按键是否启用
	 */
	public boolean getEnabled() {
		switch (config) {
			case WHILE_PRESSING:
				return now;
			case SINGLE_WHEN_PRESSED:
			default:
				return now && ! lst;
		}
	}

	/**
	 * 重写toString方法，用于返回当前按键状态的字符串表示
	 *
	 * @return 当前按键状态的字符串表示
	 */
	@NonNull
	@Override
	public String toString() {
		return "lst:" + lst + ",now:" + now;
	}
}
