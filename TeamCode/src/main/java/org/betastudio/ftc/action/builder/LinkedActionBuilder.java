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
	public Action store() {
		return new LinkedAction(actions);
	}
}
