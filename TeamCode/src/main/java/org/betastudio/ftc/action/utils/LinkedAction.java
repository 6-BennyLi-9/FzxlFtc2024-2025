package org.betastudio.ftc.action.utils;


import static org.betastudio.ftc.Interfaces.ProgressMarker;
import static org.betastudio.ftc.Interfaces.ProgressedTask;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.AbstractActionImplement;
import org.betastudio.ftc.util.ProgressMarkerImplement;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 链式的 {@code Action} 块，可以优化代码书写，减少重复代码。
 */
public final class LinkedAction extends AbstractActionImplement implements ProgressedTask {
	private final ProgressMarkerImplement marker;

	public LinkedAction(@NonNull final List <Action> actions) {
		marker = new ProgressMarkerImplement(actions.size());
		final AtomicInteger ptr = new AtomicInteger(0);
		setAction(() -> {
			if (actions.get(ptr.get()).activate()) {
				return true;
			} else {
				ptr.getAndAdd(1);
				marker.tick();
				return ptr.get() < actions.size();
			}
		});

		setParams(() -> {
			final StringBuilder stringBuilder = new StringBuilder("{");
			for (final Action action : actions) {
				stringBuilder.append(action.paramsString()).append(",");
			}
			return stringBuilder.append("}").toString();
		});

		setName(getName() + this.getClass().getSimpleName());
	}

	public LinkedAction(final Action... actions) {
		this(Arrays.asList(actions));
	}

	@Override
	public ProgressMarker getWorkerProgress() {
		return marker;
	}
}
