package org.betastudio.ftc.job;

import static org.betastudio.ftc.Annotations.MirrorMethod;

public abstract class AbstractJob implements Job{
	protected String name;

	@MirrorMethod
	@Override
	public void addDependency(Job job) {
		getDependencies().add(job);
	}

	@MirrorMethod
	@Override
	public void removeDependency(Job job) {
		getDependencies().remove(job);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name=name;
	}
}
