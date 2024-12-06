package org.betastudio.application.action.utils;

import org.betastudio.application.action.Action;
import org.betastudio.application.action.PriorityAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 根据 {@code PriorityAction} 的优先级排序后进行执行操作，如果超时后将强制退出执行链
 */
public class TimedAllottedPriorityAction implements Action {
	public final  List <PriorityAction> actions;
	private final long                  allottedMilliseconds;
	private       boolean               initialized;

	public TimedAllottedPriorityAction(final long allottedMilliseconds, final List <PriorityAction> actions) {
		this.actions = new ArrayList <>();
		this.actions.addAll(actions);
		this.actions.sort(Comparator.comparingLong(x -> - x.getPriorityCode()));
		this.allottedMilliseconds = allottedMilliseconds;
	}

	public TimedAllottedPriorityAction(final long allottedMilliseconds, final PriorityAction... actions) {
		this(allottedMilliseconds, Arrays.asList(actions));
	}

	private double startTime;

	@Override
	public boolean run() {
		if (! initialized) {
			startTime = System.nanoTime() / 1.0e6;
			initialized = true;
		}
		final Set <PriorityAction> removes = new HashSet <>();

		for (final PriorityAction action : actions) {
			if (! action.run()) {
				removes.add(action);
			}
			if (System.nanoTime() / 1.0e6 - startTime >= allottedMilliseconds) {
				break;
			}
		}

		actions.removeAll(removes);
		return ! actions.isEmpty();
	}

	@NotNull
	@Override
	public String paramsString() {
		final StringBuilder stringBuilder = new StringBuilder("tl:" + allottedMilliseconds + "{");
		for (final PriorityAction action : actions) {
			stringBuilder.append("[").append(action.getPriorityCode()).append(")").append(action.paramsString()).append(",");
		}
		return stringBuilder.append("}").toString();
	}
}
