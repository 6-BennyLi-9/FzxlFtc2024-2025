package org.betastudio.ftc.action.builder;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.action.Action;

public interface ActionBuilder extends Interfaces.StoreRequired <Action> {
	void append(Action action);

	void clear();

	void remove(Action action);
}
