package org.betastudio.ftc.action.render;

import androidx.annotation.NonNull;

import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.ui.client.Client;

public class ClientRender implements Interfaces.ProgressRender {
	private final Client client;

	public ClientRender(Client client) {
		this.client = client;
	}

	@Override
	public void render(String name, @NonNull Interfaces.ProgressMarker marker) {
		client.changeData(name, marker.getProgressString() + marker);
	}
}
