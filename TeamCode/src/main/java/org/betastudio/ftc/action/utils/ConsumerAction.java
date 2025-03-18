package org.betastudio.ftc.action.utils;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.ActionImplementFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

public class ConsumerAction<T> extends ActionImplementFactory {

	public ConsumerAction(@NonNull Collection <T> collection, Consumer <T> consumer) {
		this(collection.iterator(), consumer);
	}

	public ConsumerAction(Iterator <T> iterator, Consumer <T> consumer) {
		setAction(() -> {
			boolean hasNext = iterator.hasNext();
			if (hasNext) {
				consumer.accept(iterator.next());
			}
			return hasNext;
		});
	}
}
