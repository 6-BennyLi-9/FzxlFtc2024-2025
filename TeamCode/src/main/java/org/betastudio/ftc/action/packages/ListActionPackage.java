package org.betastudio.ftc.action.packages;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.Actions;

public class ListActionPackage extends ActionPackage {
	protected long count;

	@Override
	public void add(Action action) {
		add(Actions.newMirroredPriority(action, ++count));
	}
}
