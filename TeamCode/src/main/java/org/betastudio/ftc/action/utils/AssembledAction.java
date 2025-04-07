package org.betastudio.ftc.action.utils;


import org.betastudio.ftc.*;
import org.betastudio.ftc.action.*;
import org.betastudio.ftc.util.*;

import java.util.*;

/**
 * 多线程的 {@code Action} 块，对 {@code tps} 要求较高
 */
public final class AssembledAction extends ActionImplementFactory implements Interfaces.ProgressedTask {
	private final ProgressMarker marker;

	public AssembledAction(final List <Action> actions) {
		new LinkedList <>(actions);
		marker = new ProgressMarker(actions.size());
		setAction(()->{
			if (actions.isEmpty()) return false;
			final Collection <Action> removes = new HashSet <>();
			for (final Action action : actions) {
				if (! action.activate()) {
					removes.add(action);
					marker.tick();
				}
			}
			actions.removeAll(removes);
			return ! actions.isEmpty();
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

	public AssembledAction(final Action... actions) {
		this(new ArrayList <>(Arrays.asList(actions)));
	}

	@Override
	public Interfaces.ProgressMarker getWorkerProgress() {
		return marker;
	}
}
