package org.betastudio.ftc.job.render;

import static org.betastudio.ftc.Interfaces.ProgressRender;
import static org.betastudio.ftc.Interfaces.ProgressMarker;

import java.util.ArrayList;
import java.util.List;

public final class MultipleRender implements ProgressRender {
	private final List <ProgressRender> renders;

	public MultipleRender(ProgressRender... renders){
		this.renders = new ArrayList <>(List.of(renders));
	}

	@Override
	public void render(final String name, final ProgressMarker marker) {
		renders.forEach(render->render.render(name,marker));
	}
}
