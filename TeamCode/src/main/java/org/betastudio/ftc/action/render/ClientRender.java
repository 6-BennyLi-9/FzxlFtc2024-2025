package org.betastudio.ftc.action.render;

import static org.betastudio.ftc.Interfaces.ProgressMarker;
import static org.betastudio.ftc.Interfaces.ProgressRender;

import androidx.annotation.NonNull;

import org.betastudio.ftc.ui.client.Client;

public class ClientRender implements ProgressRender {
	private final Client client;

	public ClientRender(final Client client) {
		this.client = client;
	}

	@Override
	public void render(final String name, @NonNull final ProgressMarker marker) {
		client.changeData(name, marker.getProgressString() + marker);
	}
}
