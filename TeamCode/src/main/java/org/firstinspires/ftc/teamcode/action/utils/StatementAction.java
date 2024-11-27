package org.firstinspires.ftc.teamcode.action.utils;


import org.firstinspires.ftc.teamcode.action.Action;
import org.jetbrains.annotations.NotNull;

public class StatementAction implements Action {
	/**
	 * @implNote 如果要打印有实际意义的 {@code paramsString} 输出，需要在实现接口时重写 {@link #toString()}
	 */
	public interface StatementNode {
		void run();
	}

	private final StatementNode node;

	/**
	 * 语句式 {@code Action} 块
	 * <p>
	 * 示例： {@code ... new StatementAction( () -> yourMethodHere() ); ...}
	 */
	public StatementAction(final StatementNode meaning) {
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
