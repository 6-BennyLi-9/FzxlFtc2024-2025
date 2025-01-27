package org.betastudio.ftc.ui.client;

import androidx.annotation.NonNull;

import org.betastudio.ftc.specification.MessagesProcessRequired;
import org.betastudio.ftc.specification.Updatable;
import org.betastudio.ftc.util.message.TelemetryMsg;

/**
 * @noinspection UnusedReturnValue
 */
public interface Client extends Updatable , MessagesProcessRequired <TelemetryMsg> {
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

	void configViewMode(final ClientViewMode clientViewMode);

	void setUpdateConfig(final UpdateConfig updateConfig);

	UpdateConfig getUpdateConfig();

	ClientViewMode getCurrentViewMode();

	default void switchViewMode(){
		switch (getCurrentViewMode()){
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
}
