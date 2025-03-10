package org.betastudio.ftc.job.render;

import static org.betastudio.ftc.Interfaces.JobProgressRender;
import static org.betastudio.ftc.Interfaces.ProgressMarker;

import java.util.ArrayList;
import java.util.List;

public final class MultipleJobRender implements JobProgressRender {
	private final List <JobProgressRender> renders;

	public MultipleJobRender(JobProgressRender... renders){
		this.renders = new ArrayList <>(List.of(renders));
	}

	@Override
	public void render(final String name, final ProgressMarker marker) {
		renders.forEach(render->render.render(name,marker));
	}
}
