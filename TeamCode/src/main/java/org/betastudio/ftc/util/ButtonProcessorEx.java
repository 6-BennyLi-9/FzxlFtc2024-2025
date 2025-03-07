package org.betastudio.ftc.util;

import org.betastudio.ftc.Annotations.Beta;
import org.betastudio.ftc.ui.log.FtcLogTunnel;

@Deprecated
public class ButtonProcessorEx extends ButtonProcessor {
	@FunctionalInterface
	public interface ButtonCallback{
		void onActive();
		default void onDisabled() {
		}
	}
	public static final ButtonCallback defaultCallback = ()->{};
	@Beta(date = "2025-2-26")
	public static boolean runUsingThread;

	private ButtonCallback callback;
	private boolean		   isAutoActive;

	/**
	 * 构造函数，初始化按键处理器
	 *
	 * @param config 按键配置，定义按键如何响应
	 */
	public ButtonProcessorEx(final ButtonConfig config) {
		this(config, defaultCallback);
	}

	/**
	 * 构造函数，初始化按键处理器
	 *
	 * @param config 按键配置，定义按键如何响应
	 */
	public ButtonProcessorEx(final ButtonConfig config, final ButtonCallback callback) {
		super(config);
		this.callback = callback;
	}

	public void tryActivate(){
		final boolean enabled = getEnabled();
		FtcLogTunnel.MAIN.report("enabled=" + enabled);
		if (enabled){
			callback.onActive();
		}else{
			callback.onDisabled();
		}
	}

	public void setCallback(final ButtonCallback callback) {
		this.callback = callback;
	}

	public void setAutoActive(final boolean autoActive) {
		isAutoActive = autoActive;
	}

	@Override
	public void sync(final boolean input) {
		super.sync(input);
		if(isAutoActive){
			activeButtonMark();
		}
	}

	/// hook方法，当按键被激活时调用
	@Beta(date = "2025-2-26")
	protected void activeButtonMark(){
		tryActivate();
	}
}
