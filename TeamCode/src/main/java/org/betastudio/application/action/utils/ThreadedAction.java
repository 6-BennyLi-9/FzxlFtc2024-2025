package org.betastudio.application.action.utils;


import org.betastudio.application.action.Action;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 多线程的 {@code Action} 块，对 {@code tps} 要求较高
 */
public final class ThreadedAction implements Action {
	public final List <Action> actions;

	public ThreadedAction(final List <Action> actions) {
		this.actions = new ArrayList <>();
		this.actions.addAll(actions);
	}

	public ThreadedAction(final Action... actions) {
		this(Arrays.asList(actions));
	}

	@Override
	public boolean run() {
		if (actions.isEmpty()) return false;
		final Set <Action> removes = new HashSet <>();
		for (final Action action : actions) {
			if (! action.run()) {
				removes.add(action);
			}
		}
		actions.removeAll(removes);
		return ! actions.isEmpty();
	}

	@NotNull
	@Override
	public String paramsString() {
		final StringBuilder stringBuilder = new StringBuilder("{");
		for (final Action action : actions) {
			stringBuilder.append(action.paramsString()).append(",");
		}
		return stringBuilder.append("}").toString();
	}
}
