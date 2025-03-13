package org.betastudio.ftc.action.packages;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.Actions;
import org.betastudio.ftc.action.PriorityAction;
import org.betastudio.ftc.action.utils.LinkedAction;

import java.util.LinkedList;
import java.util.List;

public class LinkedActionPackage implements ActionPackage , Interfaces.StoreRequired <LinkedAction> {
	private final List<Action> actions;

	public LinkedActionPackage() {
		actions = new LinkedList <>();
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

	@Override
	public LinkedAction store() {
		return new LinkedAction(actions);
	}
}
