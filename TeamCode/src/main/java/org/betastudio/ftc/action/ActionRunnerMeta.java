package org.betastudio.ftc.action;

import static org.betastudio.ftc.Interfaces.Nameable;
import static org.betastudio.ftc.Interfaces.ProgressMarker;
import static org.betastudio.ftc.Interfaces.ProgressRender;
import static org.betastudio.ftc.Interfaces.ProgressedTask;

import androidx.annotation.NonNull;

import org.betastudio.ftc.util.Labeler;
import org.betastudio.ftc.util.ProgressMarkerImplement;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class ActionRunnerMeta implements Action {
	private final Action metaRunner;

	public ActionRunnerMeta(@NonNull final Action action, final ProgressRender render) {
		final AtomicReference <ProgressMarker> marker = new AtomicReference <>(new ProgressMarkerImplement(action.getCount()));
		final AtomicReference <String>         name   = new AtomicReference <>(Labeler.gen().summon(action));

		if (action instanceof Nameable) {
			name.set(((Nameable) action).getName());
		}

		AtomicBoolean res = new AtomicBoolean(false);
		if (action instanceof ProgressedTask) {
			metaRunner = () -> {
				marker.set(((ProgressedTask) action).getWorkerProgress());
				render.render(name.get(), marker.get());
				res.set(action.activate());
				if (! res.get()) {
					marker.set(((ProgressedTask) action).getWorkerProgress());
					render.render(name.get(), marker.get());
				}
				return res.get();
			};
		} else {
			metaRunner = () -> {
				marker.get().tick();
				render.render(name.get(), marker.get());
				res.set(action.activate());
				if (! res.get()) {
					marker.get().tick();
					render.render(name.get(), marker.get());
				}
				return res.get();
			};
		}
	}

	@Override
	public boolean activate() {
		return metaRunner.activate();
	}
}
