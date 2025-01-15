package org.betastudio.ftc.client;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Labeler;

import java.util.HashMap;
import java.util.Map;

public class MultiTelemetryClient implements Client {
	private final Map <String, Client> clients = new HashMap <>();
	private final Labeler              labeler = new Labeler();

	public MultiTelemetryClient(@NonNull final Client... clients) {
		for (final Client client : clients) {
			this.clients.put(labeler.summonID(client), client);
		}
	}

	public void add(final Client client) {
		add(labeler.summonID(client), client);
	}

	public void add(final String tag, final Client client) {
		clients.put(tag, client);
	}

	@Override
	public void clear() {
		for (final Map.Entry <String, Client> entry : clients.entrySet()) {
			entry.getValue().clear();
		}
	}

	@Override
	public Client addData(final String key, final String val) {
		for (final Map.Entry <String, Client> entry : clients.entrySet()) {
			entry.getValue().addData(key, val);
		}
		return this;
	}

	@Override
	public Client addData(final String key, final Object val) {
		return this.addData(key, String.valueOf(val));
	}

	@Override
	public Client deleteData(final String key) {
		for (final Map.Entry <String, Client> entry : clients.entrySet()) {
			entry.getValue().deleteData(key);
		}
		return this;
	}

	@Override
	public Client changeData(final String key, final String val) {
		for (final Map.Entry <String, Client> entry : clients.entrySet()) {
			entry.getValue().changeData(key, val);
		}
		return this;
	}

	@Override
	public Client changeData(final String key, final Object val) {
		return changeData(key, String.valueOf(val));
	}

	@Override
	public Client addLine(final String key) {
		for (final Map.Entry <String, Client> entry : clients.entrySet()) {
			entry.getValue().addLine(key);
		}
		return this;
	}

	@Override
	public Client addLine(final Object key) {
		return addLine(String.valueOf(key));
	}

	@Override
	public Client deleteLine(final String key) {
		for (final Map.Entry <String, Client> entry : clients.entrySet()) {
			entry.getValue().deleteLine(key);
		}
		return this;
	}

	@Override
	public Client changeLine(final String oldData, final String newData) {
		for (final Map.Entry <String, Client> entry : clients.entrySet()) {
			entry.getValue().changeLine(oldData, newData);
		}
		return this;
	}

	@Override
	public Client speak(String text) {
		return speak(text, null, null);
	}

	@Override
	public Client speak(String text, String languageCode, String countryCode) {
		try {
			for (final Map.Entry <String, Client> entry : clients.entrySet()) {
				entry.getValue().speak(text, languageCode, countryCode);
			}
		} catch (UnsupportedOperationException ignored) {
		}
		return this;
	}

	@Override
	public void configViewMode(ViewMode viewMode) {
		for (final Map.Entry <String, Client> entry : clients.entrySet()) {
			entry.getValue().configViewMode(viewMode);
		}
	}

	@Override
	public void setAutoUpdate(boolean autoUpdate) {
		for (final Map.Entry <String, Client> entry : clients.entrySet()) {
			entry.getValue().setAutoUpdate(autoUpdate);
		}
	}

	@Override
	public ViewMode getCurrentViewMode() {
		return ((Client) clients.entrySet().toArray()[0]).getCurrentViewMode();
	}

	@Override
	public void update() {
		for (final Map.Entry <String, Client> entry : clients.entrySet()) {
			entry.getValue().update();
		}
	}

	@Override
	public boolean isUpdateRequested() {
		boolean res = false;
		for (final Map.Entry <String, Client> entry :clients.entrySet()) {
			res=res||entry.getValue().isUpdateRequested();
		}
		return res;
	}
}
