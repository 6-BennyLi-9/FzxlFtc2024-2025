package org.betastudio.application.action.packages;

import org.betastudio.application.action.Actions;
import org.betastudio.application.action.utils.PriorityThreadedAction;

public class ThreadedActionPackage extends ActionPackage {
	@Override
	public void runTillEnd() {
		new Thread(() -> Actions.runAction(new PriorityThreadedAction(actions)));
		actions.clear();
	}

	public void linearRunTillEnd() {
		Actions.runAction(new PriorityThreadedAction(actions));
		actions.clear();
	}
}
