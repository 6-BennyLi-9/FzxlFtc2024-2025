package org.firstinspires.ftc.teamcode.cores.eventloop.commands;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.Actions;
import org.betastudio.ftc.action.utils.StatementAction;

public class ActionCommand implements Command {
	private final Action action;

	public ActionCommand(Action action) {
		this.action = action;
	}

	public ActionCommand(Runnable runnable) {
		this.action = new StatementAction(runnable);
	}

	public void execute() {
		Actions.runAction(action);
	}
}
