package org.betastudio.ftc.action.utils;

import androidx.annotation.NonNull;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.action.ActionImplementFactory;
import org.betastudio.ftc.util.ProgressMarker;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

public class ConsumerAction<T> extends ActionImplementFactory implements Interfaces.ProgressedTask {
	private final ProgressMarker marker;

	public ConsumerAction(@NonNull Collection <T> collection, Consumer <T> consumer) {
		final Iterator <T> iterator = collection.iterator();
		marker = new ProgressMarker(collection.size());

		setAction(() -> {
			boolean hasNext = iterator.hasNext();
			if (hasNext) {
				consumer.accept(iterator.next());
				marker.tick();
			}
			return hasNext;
		});
	}

	@Override
	public Interfaces.ProgressMarker getWorkerProgress() {
		return marker;
	}
}
