package org.betastudio.ftc.util;

public class ButtonProcessorEx extends ButtonProcessor {
	public interface ButtonCallback{
		void onActive();
		default void onDisabled() {
		}
	}
	private static final ButtonCallback defaultCallback = ()->{};

	private ButtonCallback callback;

	/**
	 * 构造函数，初始化按键处理器
	 *
	 * @param config 按键配置，定义按键如何响应
	 */
	public ButtonProcessorEx(ButtonConfig config) {
		this(config, defaultCallback);
	}

	/**
	 * 构造函数，初始化按键处理器
	 *
	 * @param config 按键配置，定义按键如何响应
	 */
	public ButtonProcessorEx(ButtonConfig config, ButtonCallback callback) {
		super(config);
		this.callback = callback;
	}

	public boolean tryActivate(){
		boolean enabled = getEnabled();
		if (enabled){
			callback.onActive();
		}else{
			callback.onDisabled();
		}
		return enabled;
	}

	public void setCallback(ButtonCallback callback) {
		this.callback = callback;
	}
}
