package org.betastudio.ftc.action.utils;


import org.betastudio.ftc.action.Action;

public class StatementAction implements Action {
	private final Runnable node;

	/**
	 * 语句式 {@code Action} 块
	 * <p>
	 * 示例： {@code ... new StatementAction( () -> yourMethodHere() ); ...}
	 */
	public StatementAction(final Runnable meaning) {
		node = meaning;
	}

	@Override
	public boolean run() {
		node.run();
		return false;
	}


	@Override
	public String paramsString() {
		return "statement:" + node;
	}
}
