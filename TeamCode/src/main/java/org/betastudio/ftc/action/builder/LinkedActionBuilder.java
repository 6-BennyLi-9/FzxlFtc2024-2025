package org.betastudio.ftc.action.builder;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.utils.LinkedAction;

import java.util.ArrayList;
import java.util.List;

public class LinkedActionBuilder implements ActionBuilder {
	protected final List <Action> actions;

	public LinkedActionBuilder() {
		actions = new ArrayList <>();
	}

	@Override
	public void append(final Action action) {
		actions.add(action);
	}

	@Override
	public void clear() {
		actions.clear();
	}

	@Override
	public void remove(final Action action) {
		actions.remove(action);
	}

	/**
	 * 为了防止后期出现 {@link IndexOutOfBoundsException}， 将提前断言可能出现的问题
	 */
	@Override
	public Action store() {
		assert ! actions.isEmpty();
		return new LinkedAction(new ArrayList <>(actions));
	}
}
