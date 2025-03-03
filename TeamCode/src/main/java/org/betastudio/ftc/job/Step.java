package org.betastudio.ftc.job;

import org.betastudio.ftc.action.Action;
import org.betastudio.ftc.action.utils.StatementAction;

import java.util.Collections;
import java.util.List;

public class Step implements Job {
	protected String name;
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
	public void addDependency(Job job) {
		throw new JobNotParalleledException();
	}

	@Override
	public void removeDependency(Job job) {
		throw new JobNotParalleledException();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean isParallel() {
		return false;
	}

	@Override
	public boolean activate() {
		return action.activate();
	}
}
