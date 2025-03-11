package org.betastudio.ftc.job.implementation;

import androidx.annotation.NonNull;

import org.betastudio.ftc.Annotations;
import org.betastudio.ftc.job.AbstractJob;
import org.betastudio.ftc.job.Job;
import org.betastudio.ftc.job.JobNotParalleledException;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

@Annotations.Beta(date = "2025.3.11")
public class ConsumerJob <T> extends AbstractJob {
	protected final Iterator<T> iterator;
	protected final Consumer<T> consumer;

	public ConsumerJob(@NonNull Collection <T> collection, Consumer <T> consumer) {
		this.iterator = collection.iterator();
		this.consumer = consumer;
	}

	@Override
	public List <Job> getDependencies() {
		return Collections.emptyList();
	}

	@Override
	public void addDependency(Job job) {
		throw new JobNotParalleledException();
	}

	@Override
	public void removeDependency(Job job) {
		throw new JobNotParalleledException();
	}

	@Override
	public boolean isParallel() {
		return false;
	}

	@Override
	public boolean activate() {
		boolean b = iterator.hasNext();
		if (b) {
			consumer.accept(iterator.next());
		}
		return b;
	}
}
