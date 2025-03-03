package org.betastudio.ftc.job;

import org.betastudio.ftc.action.Action;

import java.util.List;

public interface Job extends Action {
	List <Job> getDependencies();
	void addDependency(Job job);
	void removeDependency(Job job);

	String getName();
	void setName(String name);

	boolean isParallel();
}
