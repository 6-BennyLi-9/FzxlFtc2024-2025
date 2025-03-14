package org.betastudio.ftc.action.packages;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.Actions;
import org.betastudio.ftc.action.PriorityAction;

import java.util.ArrayList;
import java.util.List;

public class LinkedActionPackage implements ActionPackage {
	private final List<Action> actions;

	public LinkedActionPackage() {
		actions = new ArrayList <>();
	}

	/**
	 * 优先级不会起作用
	 */
	@Override
	public void add(PriorityAction action) {
		actions.add(action);
	}

	@Override
	public void add(Action action) {
		actions.add(action);
	}

	@Override
	public boolean activate() {
		synchronized (actions){
			if (!actions.isEmpty() && actions.get(0).activate()) {
				actions.remove(0);
			}
			return actions.isEmpty();
		}
	}

	@Override
	public void activateTillEnd() {
		Actions.runAction(this);
	}
}
