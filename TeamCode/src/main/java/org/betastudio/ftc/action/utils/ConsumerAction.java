package org.betastudio.ftc.action.utils;

import static org.betastudio.ftc.Interfaces.ProgressMarker;
import static org.betastudio.ftc.Interfaces.ProgressedTask;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.AbstractActionImplement;
import org.betastudio.ftc.util.ProgressMarkerImplement;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

public class ConsumerAction<T> extends AbstractActionImplement implements ProgressedTask {
	private final ProgressMarkerImplement marker;

	public ConsumerAction(@NonNull final Collection <T> collection, final Consumer <T> consumer) {
		final Iterator <T> iterator = collection.iterator();
		marker = new ProgressMarkerImplement(collection.size());

		setAction(() -> {
			final boolean hasNext = iterator.hasNext();
			if (hasNext) {
				consumer.accept(iterator.next());
				marker.tick();
			}
			return hasNext;
		});

		setName(getName() + this.getClass().getSimpleName());
	}

	@Override
	public ProgressMarker getWorkerProgress() {
		return marker;
	}
}
