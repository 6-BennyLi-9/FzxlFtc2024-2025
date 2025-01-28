package org.betastudio.ftc.action.utils;


import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;

import java.util.Arrays;
import java.util.List;

/**
 * 链式的 {@code Action} 块，可以优化代码书写，减少重复代码。
 */
public final class LinkedAction implements Action {
	private final List <Action> actions;
	private       int           ptr;

	public LinkedAction(final List <Action> actions) {
		this.actions = actions;
	}

	public LinkedAction(final Action... actions) {
		this(Arrays.asList(actions));
	}


	@Override
	public boolean activate() {
		if (actions.get(ptr).activate()) {
			return true;
		} else {
			ptr++;
			return ptr < actions.size();
		}
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
