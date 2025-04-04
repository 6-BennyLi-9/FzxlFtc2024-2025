package org.betastudio.ftc.action.builder;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.utils.ThreadedAction;

import java.util.ArrayList;
import java.util.List;

public class ThreadedActionBuilder implements ActionBuilder{
	protected final List <Action> actions;

	public ThreadedActionBuilder(){
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

	@Override
	public Action store() {
		return new ThreadedAction(actions);
	}
}
