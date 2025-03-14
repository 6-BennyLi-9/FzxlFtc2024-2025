package org.betastudio.ftc.action;

import org.betastudio.ftc.Interfaces;

public class ActionExpressions {
	public interface NameableAction extends Action, Interfaces.Nameable {}
	public interface ProgressRenderAbleAction extends Action, Interfaces.ValueProduction<Interfaces.ProgressRender> {}

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
}
