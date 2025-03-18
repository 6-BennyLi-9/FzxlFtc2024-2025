package org.betastudio.ftc.action;

import androidx.annotation.NonNull;

import org.betastudio.ftc.Annotations;
import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.action.render.NullptrRender;
import org.betastudio.ftc.util.Labeler;
import org.betastudio.ftc.util.ProgressMarker;
import org.jetbrains.annotations.Contract;

public final class Actions {
	@NonNull
	public static Interfaces.ProgressRender DEFAULT_RENDER = new NullptrRender();

	/**
	 * @param actionBlock 要运行的 {@code Action} 块,执行直到结束
	 * @param render      用于渲染的渲染器
	 */
	public static void runAction(@NonNull final Action actionBlock, Interfaces.ProgressRender render) {
		Interfaces.ProgressMarker marker = new ProgressMarker(actionBlock.getCount());
		String                    name   = Labeler.gen().summon(actionBlock);
		if (actionBlock instanceof Interfaces.Nameable) {
			name = ((Interfaces.Nameable) actionBlock).getName();
		}

		if (actionBlock instanceof Interfaces.ProgressedTask) {
			while (actionBlock.activate()) {
				marker = ((Interfaces.ProgressedTask) actionBlock).getWorkerProgress();
				render.render(name, marker);
			}
		} else {
			while (actionBlock.activate()) {
				marker.tick();
				render.render(name, marker);
			}
		}
	}

	/**
	 * @param actionBlock 要运行的 {@code Action} 块,执行直到结束
	 */
	@Annotations.MirrorMethod
	public static void runAction(@NonNull final Action actionBlock) {
		runAction(actionBlock, DEFAULT_RENDER);
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

	@Annotations.MirrorMethod
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
