package org.betastudio.ftc.job;

import java.util.ArrayList;
import java.util.List;

public class TreeJob implements Job {
	protected final List <Job> dependencies = new ArrayList <>();
	protected       String     name;
	protected       boolean   parallel;

	@Override
	public List <Job> getDependencies() {
		return dependencies;
	}

	@Override
	public void addDependency(Job job) {
		dependencies.add(job);
	}

	@Override
	public void removeDependency(Job job) {
		dependencies.remove(job);
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
		return dependencies.isEmpty();
	}

	@Override
	public boolean activate() {
		if(dependencies.isEmpty()){
			return false;
		} else {
			if (! dependencies.get(0).activate()) {
				dependencies.remove(0);
			}
			return true;
		}
	}
}
