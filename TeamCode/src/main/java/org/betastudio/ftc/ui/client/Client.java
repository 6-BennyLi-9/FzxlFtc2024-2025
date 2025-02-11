package org.betastudio.ftc.ui.client;

import androidx.annotation.NonNull;

import org.betastudio.ftc.util.entry.MessagesProcessRequired;
import org.betastudio.ftc.util.entry.Updatable;
import org.betastudio.ftc.util.message.TelemetryMsg;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * @noinspection UnusedReturnValue
 */
public interface Client extends MessagesProcessRequired <TelemetryMsg> , Updatable {
	void clear();

	void putData(final String key, final String val);

	void deleteData(final String key);

	void changeData(final String key, final String val);

	void putLine(final String key);

	void deleteLine(final String key);

	void changeLine(final String oldData, final String newData);

	void speak(String text, String languageCode, String countryCode);

	void configViewMode(final ClientViewMode clientViewMode);

	boolean isUpdateRequested();

	UpdateConfig getUpdateConfig();

	void setUpdateConfig(final UpdateConfig updateConfig);

	ClientViewMode getCurrentViewMode();

	Telemetry getOriginTelemetry();


	//------------------------
	// DEFAULT IMPLEMENTATION
	//------------------------
	default void switchViewMode() {
		switch (getCurrentViewMode()) {
			case ORIGIN_TELEMETRY:
				configViewMode(ClientViewMode.FTC_LOG);
				break;
			case FTC_LOG:
				configViewMode(ClientViewMode.ORIGIN_TELEMETRY);
				break;
		}
	}

	default void putData(final String key, @NonNull final Object val) {
		putData(key, val.toString());
	}

	default void changeData(final String key, @NonNull final Object val) {
		changeData(key, val.toString());
	}

	default void putLine(@NonNull final Object key) {
		putLine(key.toString());
	}

	default void speak(final String text) {
		speak(text, null, null);
	}
}
