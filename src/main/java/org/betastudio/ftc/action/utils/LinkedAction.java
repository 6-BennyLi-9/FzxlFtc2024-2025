package org.betastudio.ftc.action.utils;


import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.ActionImpl;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 链式的 {@code Action} 块，可以优化代码书写，减少重复代码。
 */
public final class LinkedAction extends ActionImpl {
	private final List <Action> actions;
	private final AtomicInteger ptr = new AtomicInteger(0);

	public LinkedAction(final List <Action> actions) {
		this.actions = actions;
		setAction(()->{
			if (actions.get(ptr.get()).activate()) {
				return true;
			} else {
				ptr.getAndAdd(1);
				return ptr.get() < actions.size();
			}
		});
	}

	public LinkedAction(final Action... actions) {
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
