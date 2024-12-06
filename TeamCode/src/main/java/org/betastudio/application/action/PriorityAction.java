package org.betastudio.application.action;

/**
 * 带有优先级编码的模块化程序块，继承自 {@code Action}
 *
 * @see Action
 */
public interface PriorityAction extends Action {
	/**
	 * @return 优先级，数值越大优先级越高
	 */
	long getPriorityCode();

	@Override
	default String paramsString() {
		return Action.super.paramsString() + "->" + this.hashCode();
	}
}
