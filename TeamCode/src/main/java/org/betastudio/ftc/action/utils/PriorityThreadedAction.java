package org.betastudio.ftc.action.utils;


import org.betastudio.ftc.action.ActionExpressions;
import org.betastudio.ftc.action.ActionImplementFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 根据 {@code PriorityAction} 的优先级排序后进行执行操作
 */
public class PriorityThreadedAction extends ActionImplementFactory {
	public final List <ActionExpressions.PriorityAction> actions;

	public PriorityThreadedAction(final List <ActionExpressions.PriorityAction> actions) {
		this.actions = new ArrayList <>();
		this.actions.addAll(actions);
		this.actions.sort(Comparator.comparingLong(x -> - x.getPriorityCode()));
		setAction(()->{
			final Set <ActionExpressions.PriorityAction> removes = new HashSet <>();
			for (final ActionExpressions.PriorityAction action : actions) {
				if (! action.activate()) {
					removes.add(action);
				}
			}
			actions.removeAll(removes);
			return ! actions.isEmpty();
		});
	}

	public PriorityThreadedAction(final ActionExpressions.PriorityAction... actions) {
		this(Arrays.asList(actions));
	}


	@Override
	public String paramsString() {
		final StringBuilder stringBuilder = new StringBuilder("{");
		for (final ActionExpressions.PriorityAction action : actions) {
			stringBuilder.append("[").append(action.getPriorityCode()).append(")").append(action.paramsString()).append(",");
		}
		return stringBuilder.append("}").toString();
	}
}
