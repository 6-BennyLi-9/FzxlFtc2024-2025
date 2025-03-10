package org.betastudio.ftc.action.utils;

import org.betastudio.ftc.action.ActionImpl;
import org.betastudio.ftc.action.PriorityAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 根据 {@code PriorityAction} 的优先级排序后进行执行操作，如果超时后将强制退出执行链
 */
public class TimedAllottedPriorityAction extends ActionImpl {
	public final  List <PriorityAction> actions;
	private final long                  allottedMilliseconds;
	private       boolean               initialized;
	private       double                startTime;

	public TimedAllottedPriorityAction(final long allottedMilliseconds, final List <PriorityAction> actions) {
		this.actions = new ArrayList <>();
		this.actions.addAll(actions);
		this.actions.sort(Comparator.comparingLong(x -> - x.getPriorityCode()));
		this.allottedMilliseconds = allottedMilliseconds;
		setAction(()->{
			if (! initialized) {
				startTime = System.nanoTime() / 1.0e6;
				initialized = true;
			}
			final Set <PriorityAction> removes = new HashSet <>();

			for (final PriorityAction action : actions) {
				if (! action.activate()) {
					removes.add(action);
				}
				if (System.nanoTime() / 1.0e6 - startTime >= allottedMilliseconds) {
					break;
				}
			}

			actions.removeAll(removes);
			return ! actions.isEmpty();
		});
	}

	public TimedAllottedPriorityAction(final long allottedMilliseconds, final PriorityAction... actions) {
		this(allottedMilliseconds, Arrays.asList(actions));
	}

	@Override
	public String paramsString() {
		final StringBuilder stringBuilder = new StringBuilder("tl:" + allottedMilliseconds + "{");
		for (final PriorityAction action : actions) {
			stringBuilder.append("[").append(action.getPriorityCode()).append(")").append(action.paramsString()).append(",");
		}
		return stringBuilder.append("}").toString();
	}
}
