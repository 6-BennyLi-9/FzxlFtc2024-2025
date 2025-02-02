package org.betastudio.ftc.action.utils;


import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.ActionImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * 多线程的 {@code Action} 块，对 {@code tps} 要求较高
 */
public final class ThreadedAction extends ActionImpl {
	public final List <Action> actions;

	public ThreadedAction(final List <Action> actions) {
		this.actions = new ArrayList <>();
		this.actions.addAll(actions);
		setAction(()->{
			if (actions.isEmpty()) return false;
			final Collection <Action> removes = new HashSet <>();
			for (final Action action : actions) {
				if (! action.activate()) {
					removes.add(action);
				}
			}
			actions.removeAll(removes);
			return ! actions.isEmpty();
		});
	}

	public ThreadedAction(final Action... actions) {
		this(Arrays.asList(actions));
	}

	@NonNull
	@Override
	public String paramsString() {
		final StringBuilder stringBuilder = new StringBuilder("{");
		for (final Action action : actions) {
			stringBuilder.append(action.paramsString()).append(",");
		}
		return stringBuilder.append("}").toString();
	}
}
