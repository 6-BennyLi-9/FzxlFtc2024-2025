package org.betastudio.ftc.action.render;

import static org.betastudio.ftc.Interfaces.ProgressMarker;
import static org.betastudio.ftc.Interfaces.ProgressRender;

public final class NullptrRender implements ProgressRender {
	@Override
	public void render(final String name, final ProgressMarker marker) {
	}
}
