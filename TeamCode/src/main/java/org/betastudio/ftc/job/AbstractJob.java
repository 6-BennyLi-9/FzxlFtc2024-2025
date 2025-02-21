package org.betastudio.ftc.job;

import java.util.Collection;
import java.util.Collections;

public abstract class AbstractJob implements Job{
	protected Collection<Job> dependencies;
	protected String name;

	@Override
	public void run() {

	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public void setName(String name) {

	}

	@Override
	public Collection <Job> getDependencies() {
		return Collections.emptyList();
	}

	@Override
	public void addDependency(Job job) {

	}

	@Override
	public void removeDependency(Job job) {

	}
}
