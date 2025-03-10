package org.betastudio.ftc.job.implementation;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.job.AbstractJob;
import org.betastudio.ftc.job.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class TreeJob extends AbstractJob implements Interfaces.Countable, Interfaces.StoreRequired<StoredJob> {
	protected final List <Job> dependencies = new ArrayList <>();

	@Override
	public List <Job> getDependencies() {
		return dependencies;
	}

	@Override
	public boolean isParallel() {
		return !dependencies.isEmpty();
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

	@Override
	public long getCount() {
		AtomicLong res = new AtomicLong();
		dependencies.forEach(job -> {
			if (job instanceof Interfaces.Countable){
				res.addAndGet(((Interfaces.Countable) job).getCount());
			} else {
				res.addAndGet(1);
			}
		});
		return res.get();
	}

	@Override
	public StoredJob store() {
		return new StoredJob(this);
	}
}
