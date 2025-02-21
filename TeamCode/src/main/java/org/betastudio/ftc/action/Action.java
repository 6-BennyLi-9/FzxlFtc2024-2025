package org.betastudio.ftc.action;

import java.util.concurrent.Callable;

/**
 * 基本的模块化程序块，可以应用于多方面的维护需求
 */
@FunctionalInterface
public interface Action extends Runnable, Callable<Boolean> {
	/**
	 * @return 该 {@code Action} 块是否结束. 为真时,该 {@code Action} 未结束,反之亦然.
	 */
	boolean activate();

	/**
	 * @return 该 {@code Action} 块的参数输出，类似于 {@code toString()}
	 * @implNote 默认打印 {@code hashCode()}，非常没用，所以要记得重写此函数来显示有意义的数据
	 */
	default String paramsString() {
		return String.valueOf(this.hashCode());
	}

	/**
	 * 运行 {@code Action} 块，调用 {@link Actions#runAction(Action)} 的委托方法
	 */
	@Override
	default void run() {
		Actions.runAction(this);
	}

	@Override
	default Boolean call() {
		return activate();
	}
}
