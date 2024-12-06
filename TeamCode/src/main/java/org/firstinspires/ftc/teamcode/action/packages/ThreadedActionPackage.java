package org.firstinspires.ftc.teamcode.action.packages;

import org.firstinspires.ftc.teamcode.action.Actions;
import org.firstinspires.ftc.teamcode.action.utils.PriorityThreadedAction;

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
