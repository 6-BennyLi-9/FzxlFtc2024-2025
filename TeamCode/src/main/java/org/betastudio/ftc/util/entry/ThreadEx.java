package org.betastudio.ftc.util.entry;

public interface ThreadEx {
	/**
	 * 安全的结束器，例如发送结束信号
	 */
	void closeTask();
}
