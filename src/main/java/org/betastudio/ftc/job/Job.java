package org.betastudio.ftc.job;

import java.util.Collection;

public interface Job extends Runnable{
	Collection<Job> getDependencies();
	void addDependency(Job job);
	void removeDependency(Job job);

	String getName();
	void setName(String name);
}
