package org.betastudio.ftc.action.utils;


import androidx.annotation.NonNull;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.ActionImplementFactory;
import org.betastudio.ftc.util.ProgressMarker;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 链式的 {@code Action} 块，可以优化代码书写，减少重复代码。
 */
public final class LinkedAction extends ActionImplementFactory implements Interfaces.ProgressedTask {
	private final List <Action> actions;
	private final AtomicInteger ptr = new AtomicInteger(0);
	private final ProgressMarker marker;

	public LinkedAction(final List <Action> actions) {
		this.actions = actions;
		marker = new ProgressMarker(actions.size());
		setAction(()->{
			if (actions.get(ptr.get()).activate()) {
				return true;
			} else {
				ptr.getAndAdd(1);
				marker.tick();
				return ptr.get() < actions.size();
			}
		});
	}

	public LinkedAction(final Action... actions) {
		this(Arrays.asList(actions));
	}

	@NonNull
	@Override
	public String paramsString() {
		final StringBuilder stringBuilder = new StringBuilder("{");
		for (final Action action : actions) {
			stringBuilder.append(action.paramsString()).append(",");
		}
		return stringBuilder.append("}").toString();
	}

	@Override
	public Interfaces.ProgressMarker getWorkerProgress() {
		return marker;
	}
}
