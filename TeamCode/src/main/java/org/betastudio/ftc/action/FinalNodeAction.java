package org.betastudio.ftc.action;

/**
 * 标志 {@code Action} 块的最后一个节点
 */
public final class FinalNodeAction implements Action {
	@Override
	public boolean run() {
		throw new FinalNodeActionInUsage("DO NOT RUN THE FinalNodeAction !!!");
	}
}
