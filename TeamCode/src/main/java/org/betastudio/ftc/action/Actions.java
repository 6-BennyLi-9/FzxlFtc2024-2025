package org.betastudio.ftc.action;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public final class Actions {
	/**
	 * @param actionBlock 要运行的 {@code Action} 块,执行直到结束
	 */
	public static void runAction(@NonNull final Action actionBlock) {
		while (true) {
			if (! actionBlock.activate()) {
				break;
			}
		}
	}

	public static void runYieldAction(@NonNull final Action actionBlock){
		while (actionBlock.activate()) {
			Thread.yield();
		}
	}

	public static void runThreadingAction(final Action actionBlock) {
		new Thread(() -> runYieldAction(actionBlock)).start();
	}

	/**
	 * 在规定时间内，如果该 {@code Action} 块仍没有结束，将会强制停止
	 *
	 * @see #runAction(Action)
	 */
	public static void runTimedAllottedAction(final Action actionBlock, final long allottedMilliseconds) {
		final double start = System.nanoTime() / 1.0e6;
		while (! (System.nanoTime() / 1.0e6 - start >= allottedMilliseconds)) {
			if (! actionBlock.activate()) {
				break;
			}
		}
	}

	@NonNull
	@Contract("_ -> new")
	public static PriorityAction newMirroredPriority(final Action action) {
		return newMirroredPriority(action, 0);
	}

	@NonNull
	@Contract("_, _ -> new")
	public static PriorityAction newMirroredPriority(final Action action, final long priorityGrade) {
		return new PriorityAction() {
			@Override
			public long getPriorityCode() {
				return priorityGrade;
			}

			@Override
			public boolean activate() {
				return action.activate();
			}

			@Override
			public String paramsString() {
				return action.paramsString();
			}
		};
	}
}
