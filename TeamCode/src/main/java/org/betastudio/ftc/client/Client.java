package org.betastudio.ftc.client;

import org.betastudio.ftc.util.MessagesProcessRequired;
import org.betastudio.ftc.util.Updatable;
import org.betastudio.ftc.message.TelemetryMessage;

/**
 * @noinspection UnusedReturnValue
 */
public interface Client extends Updatable , MessagesProcessRequired <TelemetryMessage> {
	void clear();

	Client addData(String key, String val);

	Client addData(String key, Object val);

	Client deleteData(String key);

	Client changeData(String key, String val);

	Client changeData(String key, Object val);

	Client addLine(String key);

	Client addLine(Object key);

	Client deleteLine(String key);

	Client changeLine(String oldData, String newData);

	Client speak(String text);

	Client speak(String text, String languageCode, String countryCode);

	void configViewMode(ViewMode viewMode);

	void setAutoUpdate(boolean autoUpdate);

	ViewMode getCurrentViewMode();
}
