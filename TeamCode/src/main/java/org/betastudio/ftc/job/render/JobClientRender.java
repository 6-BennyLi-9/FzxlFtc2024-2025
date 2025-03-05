package org.betastudio.ftc.job.render;

import androidx.annotation.NonNull;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.ui.client.Client;

import java.util.Locale;

public class JobClientRender implements Interfaces.JobProgressRender {
	private final Client client;

	public JobClientRender(Client client) {
		this.client = client;
	}

	@Override
	public void render(@NonNull Interfaces.ProgressMarker marker) {
		client.changeData("progress", String.format(
				Locale.SIMPLIFIED_CHINESE,
				"%d/%d[%.2f%%]",
				marker.getDone(),
				marker.getTotal(),
				marker.getProgress() * 100
		));
		client.changeData("detail", marker.getProgressString());
	}
}
