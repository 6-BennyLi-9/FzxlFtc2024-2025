package org.betastudio.application.action.utils;


import org.betastudio.application.action.Action;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * 链式的 {@code Action} 块，可以优化代码书写，可悲的是除了这个功能就没啥好处了，毕竟 {@code Action} 块是自带链式功能的
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
	public boolean run() {
		if (actions.get(ptr).run()) {
			return true;
		} else {
			ptr++;
			return ptr < actions.size();
		}
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
