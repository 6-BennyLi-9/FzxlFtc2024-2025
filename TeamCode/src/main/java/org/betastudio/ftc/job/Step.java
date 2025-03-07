package org.betastudio.ftc.job;

import java.util.Collection;

public interface Step extends Job {
	@Override
	default void removeDependency(final Job job) {
	}

	@Override
	default void addDependency(final Job job) {
	}

	@Override
	default Collection <Job> getDependencies() {
		return null;
	}
}
