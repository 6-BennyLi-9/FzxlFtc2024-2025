package org.betastudio.ftc.job;

import androidx.annotation.NonNull;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.util.ProgressMarker;
import org.firstinspires.ftc.robotcore.external.Func;

import java.util.ArrayList;
import java.util.List;

public class StoredJob implements Job, Func<Interfaces.ProgressMarker> {
	protected final List <Job> dependencies = new ArrayList <>();
	protected       String     name;
	protected       Interfaces.ProgressMarker progressMarker;

	public StoredJob (@NonNull Job job) {
		name = job.getName();
		if (job.isParallel()) {
			dependencies.addAll(job.getDependencies());
		} else {
			dependencies.add(job);
		}

		if (job instanceof Interfaces.Countable){
			progressMarker = new ProgressMarker(((Interfaces.Countable) job).getCount());
		}else{
			progressMarker = new ProgressMarker(1);
		}
	}

	@Override
	public List <Job> getDependencies() {
		return dependencies;
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
		if(dependencies.isEmpty()){
			return false;
		} else {
			if (! dependencies.get(0).activate()) {
				dependencies.remove(0);
				progressMarker.tick();
			}
			return true;
		}
	}

	@Override
	public Interfaces.ProgressMarker value() {
		return progressMarker;
	}
}
