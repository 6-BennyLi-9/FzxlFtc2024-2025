package org.betastudio.ftc.action;

import static org.betastudio.ftc.Interfaces.Nameable;
import static org.betastudio.ftc.Interfaces.ProgressMarker;
import static org.betastudio.ftc.Interfaces.ProgressRender;
import static org.betastudio.ftc.Interfaces.ProgressedTask;

import androidx.annotation.NonNull;

import org.betastudio.ftc.util.ExceptionsUtil;
import org.betastudio.ftc.util.Labeler;
import org.betastudio.ftc.util.ProgressMarkerImplement;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class ActionRunnerMeta implements Action {
	private final Action metaRunner;

	public ActionRunnerMeta(@NonNull final Action action, final ProgressRender render) {
		final AtomicReference <ProgressMarker> marker = new AtomicReference <>(new ProgressMarkerImplement(action.getCount()));
		final AtomicReference <String>         name   = new AtomicReference <>(Labeler.summon(action));

		if (action instanceof Nameable) {
			name.set(((Nameable) action).getName());
		}

		final AtomicBoolean res                    = new AtomicBoolean(false);
		final Runnable      workerProgressOverride = action instanceof ProgressedTask ? (() -> marker.set(((ProgressedTask) action).getWorkerProgress())) : (() -> marker.get().tick());
		metaRunner = () -> {
			try {
				workerProgressOverride.run();
				render.render(name.get(), marker.get());
				res.set(action.activate());
				if (! res.get()) {
					workerProgressOverride.run();
					render.render(name.get(), marker.get());
				}
				return res.get();
			} catch (Exception e) {
				Throwable cause = ExceptionsUtil.getOriginException(e);
				throw new RuntimeException(cause);
			}
		};
	}

	@Override
	public boolean activate() {
		return metaRunner.activate();
	}
}
