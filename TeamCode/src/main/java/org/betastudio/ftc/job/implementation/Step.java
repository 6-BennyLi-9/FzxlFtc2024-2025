package org.betastudio.ftc.job.implementation;

import androidx.annotation.NonNull;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.utils.StatementAction;
import org.betastudio.ftc.job.AbstractJob;
import org.betastudio.ftc.job.Job;
import org.betastudio.ftc.job.JobNotParalleledException;

import java.util.Collections;
import java.util.List;

public class Step extends AbstractJob {
	protected final Action action;

	public Step(Runnable runnable) {
		this(new StatementAction(runnable));
	}

	public Step(Action action) {
		this.action = action;
	}

	@Override
	public List <Job> getDependencies() {
		return Collections.emptyList();
	}

	@Override
	public void addDependency(final Job job) {
		throw new JobNotParalleledException();
	}

	@Override
	public void removeDependency(final Job job) {
		throw new JobNotParalleledException();
	}

	@Override
	public boolean isParallel() {
		return false;
	}

	@Override
	public boolean activate() {
		return action.activate();
	}

	@NonNull
	@Override
	public String toString() {
		return name + ':' + action.paramsString();
	}
}
