package org.betastudio.application.action;

/**
 * 基本的模块化程序块，可以应用于多方面的维护需求
 */
public interface Action {
	/**
	 * @return 该 {@code Action} 块是否结束. 为真时,该 {@code Action} 未结束,反之亦然.
	 */
	boolean run();

	/**
	 * @return 定向的下一个 {@code Action} 块，默认为 {@code FinalNodeAction}
	 * @see Actions.FinalNodeAction
	 */
	default Action next() {
		return new Actions.FinalNodeAction();
	}

	/**
	 * @return 该 {@code Action} 块的参数输出，类似于 {@code toString()}
	 * @implNote 默认打印 {@code hashCode()}，非常没用，所以要记得重写此函数来显示有意义的数据
	 */
	default String paramsString() {
		return String.valueOf(this.hashCode());
	}
}
