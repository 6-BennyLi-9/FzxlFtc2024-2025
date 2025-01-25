package org.betastudio.ftc.client;

import androidx.annotation.NonNull;

import org.betastudio.ftc.specification.MessagesProcessRequired;
import org.betastudio.ftc.specification.Updatable;
import org.betastudio.ftc.message.TelemetryMessage;

/**
 * @noinspection UnusedReturnValue
 */
public interface Client extends Updatable , MessagesProcessRequired <TelemetryMessage> {
	void clear();

	Client addData(final String key, final String val);

	default Client addData(final String key, @NonNull final Object val){
		return addData(key, val.toString());
	}

	Client deleteData(final String key);

	Client changeData(final String key, final String val);

	default Client changeData(final String key, @NonNull final Object val){
		return changeData(key, val.toString());
	}

	Client addLine(final String key);

	default Client addLine(@NonNull final Object key){
		return addLine(key.toString());
	}

	Client deleteLine(final String key);

	Client changeLine(final String oldData, final String newData);

	Client speak(final String text);

	Client speak(final String text, final String languageCode, final String countryCode);

	void configViewMode(final ViewMode viewMode);

	void setUpdateConfig(final UpdateConfig updateConfig);

	UpdateConfig getUpdateConfig();

	ViewMode getCurrentViewMode();
}
