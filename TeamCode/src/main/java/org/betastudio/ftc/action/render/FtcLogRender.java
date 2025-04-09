package org.betastudio.ftc.action.render;

import androidx.annotation.NonNull;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.ui.log.FtcLogTunnel;

import java.util.ArrayList;
import java.util.List;

public class FtcLogRender implements Interfaces.ProgressRender {
	public static List <Double> marks;

	static {
		marks = new ArrayList <>();
		marks.add(0.0);
		marks.add(0.5);
		marks.add(1.0);
		marks.add(1.2);
	}

	public        List <Double> unMarked;

	public FtcLogRender() {
		unMarked = new ArrayList <>(marks);
	}

	@Override
	public void render(final String name, @NonNull final Interfaces.ProgressMarker marker) {
		if (marker.getProgress() >= unMarked.get(0)) {
			FtcLogTunnel.MAIN.report(name + marker.getProgressString() + marker);
			unMarked.remove(0);
		}
	}
}
