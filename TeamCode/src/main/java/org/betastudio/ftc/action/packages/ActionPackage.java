package org.betastudio.ftc.action.packages;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.PriorityAction;

public interface ActionPackage extends Action {
	void add(PriorityAction action);

	void add(Action action);

	void activateTillEnd();
}
