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

	Client putData(final String key, final String val);

	Client deleteData(final String key);

	Client changeData(final String key, final String val);

	Client putLine(final String key);

	Client deleteLine(final String key);

	Client changeLine(final String oldData, final String newData);

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
				configViewMode(ClientViewMode.THREAD_MANAGER);
				break;
			case THREAD_MANAGER:
				configViewMode(ClientViewMode.FTC_LOG);
				break;
			case FTC_LOG:
				configViewMode(ClientViewMode.ORIGIN_TELEMETRY);
				break;
		}
	}

	default Client putData(final String key, @NonNull final Object val) {
		return putData(key, val.toString());
	}

	default Client changeData(final String key, @NonNull final Object val) {
		return changeData(key, val.toString());
	}

	default Client putLine(@NonNull final Object key) {
		return putLine(key.toString());
	}

	default void speak(final String text) {
		speak(text, null, null);
	}
}
