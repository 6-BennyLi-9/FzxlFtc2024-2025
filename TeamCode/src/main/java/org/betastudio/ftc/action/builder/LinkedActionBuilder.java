package org.betastudio.ftc.action.builder;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.utils.LinkedAction;

import java.util.ArrayList;
import java.util.List;

public class LinkedActionBuilder implements ActionBuilder{
	protected final List <Action> actions;

	public LinkedActionBuilder(){
		actions = new ArrayList <>();
	}

	@Override
	public void append(Action action) {
		actions.add(action);
	}

	@Override
	public void clear() {
		actions.clear();
	}

	@Override
	public void remove(Action action) {
		actions.remove(action);
	}

	@Override
	public Action store() {
		return new LinkedAction(actions);
	}
}
