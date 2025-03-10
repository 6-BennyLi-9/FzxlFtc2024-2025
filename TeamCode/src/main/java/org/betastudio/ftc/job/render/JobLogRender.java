package org.betastudio.ftc.job.render;

import androidx.annotation.NonNull;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.ui.log.FtcLogTunnel;

import java.util.Locale;

public class JobLogRender implements Interfaces.JobProgressRender {
	@Override
	public void render(String name, @NonNull Interfaces.ProgressMarker marker) {
		FtcLogTunnel.MAIN.report(String.format(Locale.SIMPLIFIED_CHINESE,
				"%s-progress:%d/%d[%.2f%%]",
				name,
				marker.getDone(),
				marker.getTotal(),
				marker.getProgress() * 100)
		);
	}
}
