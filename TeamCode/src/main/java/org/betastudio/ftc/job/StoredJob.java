package org.betastudio.ftc.job;

import androidx.annotation.NonNull;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.job.render.IgnoredJobProgressRender;
import org.betastudio.ftc.util.Labeler;
import org.betastudio.ftc.util.ProgressMarker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class StoredJob implements RenderedJob, Interfaces.Countable {
	protected final List <Job>                dependencies = new ArrayList <>();
	protected       String                    name;
	protected       Interfaces.ProgressMarker progressMarker;

	public StoredJob (@NonNull Job job) {
		name = job.getName();
		boolean parallel = job.isParallel();
		if (parallel) {
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

	public StoredJob (Job... jobs) {
		this(new ArrayList <>(Arrays.asList(jobs)));
	}

	public StoredJob (@NonNull List <Job> jobs) {
		this(Labeler.gen().summon(jobs), jobs);
	}

	public StoredJob (String name, @NonNull List <Job> jobs) {
		this.name = name;
		dependencies.addAll(jobs);
		progressMarker = new ProgressMarker(jobs.size());
	}

	@Override
	public List <Job> getDependencies() {
		return dependencies;
	}

	@Override
	public void addDependency(Job job) {
		throw new StoredException();
	}

	@Override
	public void removeDependency(Job job) {
		throw new StoredException();
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
		return activeWithRender(new IgnoredJobProgressRender());
	}

	@Override
	public boolean activeWithRender(Interfaces.JobProgressRender render) {
		if(dependencies.isEmpty()){
			return false;
		} else {
			boolean activate = Workflows.activeJobSync(dependencies.get(0),render);
			if (! activate) {
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
}
