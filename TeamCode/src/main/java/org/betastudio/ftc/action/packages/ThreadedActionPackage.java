package org.betastudio.ftc.action.packages;

import org.betastudio.ftc.action.Actions;
import org.betastudio.ftc.action.utils.PriorityThreadedAction;

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
