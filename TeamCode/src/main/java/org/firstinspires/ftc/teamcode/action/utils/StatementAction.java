package org.firstinspires.ftc.teamcode.action.utils;


import org.firstinspires.ftc.teamcode.action.Action;
import org.jetbrains.annotations.NotNull;

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

	@NotNull
	@Override
	public String paramsString() {
		return "statement:" + node;
	}
}
