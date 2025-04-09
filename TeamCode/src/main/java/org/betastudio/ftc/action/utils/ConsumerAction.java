package org.betastudio.ftc.action.utils;

import androidx.annotation.NonNull;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.action.ActionImplementFactory;
import org.betastudio.ftc.util.ProgressMarkerImplement;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

public class ConsumerAction<T> extends ActionImplementFactory implements Interfaces.ProgressedTask {
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
	public Interfaces.ProgressMarker getWorkerProgress() {
		return marker;
	}
}
