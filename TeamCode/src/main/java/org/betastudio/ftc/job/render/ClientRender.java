package org.betastudio.ftc.job.render;

import androidx.annotation.NonNull;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.ui.client.Client;

import java.util.Locale;

public class ClientRender implements Interfaces.ProgressRender {
	private final Client client;

	public ClientRender(Client client) {
		this.client = client;
	}

	@Override
	public void render(String name, @NonNull Interfaces.ProgressMarker marker) {
		client.changeData(name + "-progress", String.format(
				Locale.SIMPLIFIED_CHINESE,
				"%d/%d[%.2f%%]",
				marker.getDone(),
				marker.getTotal(),
				marker.getProgress() * 100
		));
		client.changeData(name + "-detail", marker.getProgressString());
	}
}
