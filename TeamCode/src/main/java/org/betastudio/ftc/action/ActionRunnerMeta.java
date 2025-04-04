package org.betastudio.ftc.action;

import androidx.annotation.NonNull;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.util.Labeler;
import org.betastudio.ftc.util.ProgressMarker;

import java.util.concurrent.atomic.AtomicReference;

public final class ActionRunnerMeta implements Action {
	private final Action metaRunner;

	public ActionRunnerMeta(@NonNull final Action action, final Interfaces.ProgressRender render) {
		final AtomicReference <Interfaces.ProgressMarker> marker = new AtomicReference <>(new ProgressMarker(action.getCount()));
		final AtomicReference <String>                    name   = new AtomicReference <>(Labeler.gen().summon(action));

		if (action instanceof Interfaces.Nameable) {
			name.set(((Interfaces.Nameable) action).getName());
		}

		if (action instanceof Interfaces.ProgressedTask) {
			metaRunner = () -> {
				marker.set(((Interfaces.ProgressedTask) action).getWorkerProgress());
				render.render(name.get(), marker.get());
				return action.activate();
			};
		} else {
			metaRunner = () -> {
				marker.get().tick();
				render.render(name.get(), marker.get());
				return action.activate();
			};
		}
	}

	@Override
	public boolean activate() {
		return metaRunner.activate();
	}
}
