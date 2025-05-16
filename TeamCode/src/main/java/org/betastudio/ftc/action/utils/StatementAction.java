package org.betastudio.ftc.action.utils;

import static org.betastudio.ftc.Interfaces.ProgressMarker;
import static org.betastudio.ftc.Interfaces.ProgressedTask;

import org.betastudio.ftc.action.AbstractActionImplement;
import org.betastudio.ftc.util.ProgressMarkerImplement;

public final class StatementAction extends AbstractActionImplement implements ProgressedTask {
	private final ProgressMarkerImplement marker;

	/**
	 * 语句式 {@code Action} 块
	 * <p>
	 * 示例： {@code ... new StatementAction( () -> yourMethodHere() ); ...}
	 */
	public StatementAction(final Runnable meaning) {
		marker = new ProgressMarkerImplement(1);

		setAction(() -> {
			meaning.run();
			marker.tick();
			return false;
		});

		setParams(() -> "statement:" + meaning);

		setName(getName() + this.getClass().getSimpleName());
	}

	@Override
	public ProgressMarker getWorkerProgress() {
		return marker;
	}
}
