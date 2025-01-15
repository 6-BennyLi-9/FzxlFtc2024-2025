package org.betastudio.ftc.client;

import androidx.annotation.NonNull;

import org.betastudio.ftc.interfaces.InstanceRequired;
import org.jetbrains.annotations.Contract;

public final class Clients implements Client , InstanceRequired<Client> {
	private static Client instance;

	@Override
	public Client getInstance() {
		return instance;
	}

	@Override
	public void setInstance(Client client) {
		instance = client;
	}

	@NonNull
	@Contract(" -> new")
	public static Clients generate() {
		return new Clients();
	}

	@Override
	public void clear() {
		instance.clear();
	}

	@Override
	public Client addData(String key, String val) {
		return instance.addData(key, val);
	}

	@Override
	public Client addData(String key, Object val) {
		return instance.addData(key, val);
	}

	@Override
	public Client deleteData(String key) {
		return instance.deleteData(key);
	}

	@Override
	public Client changeData(String key, String val) {
		return instance.changeData(key, val);
	}

	@Override
	public Client changeData(String key, Object val) {
		return instance.changeData(key, val);
	}

	@Override
	public Client addLine(String key) {
		return instance.addLine(key);
	}

	@Override
	public Client addLine(Object key) {
		return instance.addLine(key);
	}

	@Override
	public Client deleteLine(String key) {
		return instance.deleteLine(key);
	}

	@Override
	public Client changeLine(String oldData, String newData) {
		return instance.changeLine(oldData, newData);
	}

	@Override
	public Client speak(String text) {
		return instance.speak(text);
	}

	@Override
	public Client speak(String text, String languageCode, String countryCode) {
		return instance.speak(text, languageCode, countryCode);
	}

	@Override
	public void configViewMode(ViewMode viewMode) {
		instance.configViewMode(viewMode);
	}

	@Override
	public void setAutoUpdate(boolean autoUpdate) {
		instance.setAutoUpdate(autoUpdate);
	}

	@Override
	public ViewMode getCurrentViewMode() {
		return instance.getCurrentViewMode();
	}

	@Override
	public void update() {
		instance.update();
	}

	@Override
	public boolean isUpdateRequested() {
		throw new UnsupportedOperationException();
	}
}
