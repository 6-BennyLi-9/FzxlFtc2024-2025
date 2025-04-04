package org.betastudio.ftc.action.utils;

import org.betastudio.ftc.action.ActionImplementFactory;

public final class StatementAction extends ActionImplementFactory {
	/**
	 * 语句式 {@code Action} 块
	 * <p>
	 * 示例： {@code ... new StatementAction( () -> yourMethodHere() ); ...}
	 */
	public StatementAction(final Runnable meaning) {
		setAction(() -> {
			meaning.run();
			return false;
		});

		setParams(() -> "statement:" + meaning);

		setName(getName() + this.getClass().getSimpleName());
	}
}
