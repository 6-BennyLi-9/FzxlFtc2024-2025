package org.betastudio.ftc.job;

public abstract class AbstractJob implements Job{
	protected String name;

	@Override
	public void addDependency(Job job) {
		getDependencies().add(job);
	}

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
