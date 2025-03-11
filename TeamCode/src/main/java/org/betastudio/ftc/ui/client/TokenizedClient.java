package org.betastudio.ftc.ui.client;

import androidx.annotation.NonNull;

import org.betastudio.ftc.ui.telemetry.TelemetryElement;

public interface TokenizedClient extends Client{
	void putData(final String token,final String key, final String val);
	void putLine(final String token, final String val);
	default void putData(final String token, final String key, @NonNull final Object val){
		putData(token, key, val.toString());
	}

	TelemetryElement getByToken(final String token);
	void deleteByToken(final String token);
	void changeByToken(final String token, final String val);

	@Override
	default void putData(final String key, final String val){
		putData(key, key, val);
	}

	@Override
	@Deprecated
	void deleteData(final String key);

	@Override
	default void putLine(final String key){
		putLine(key, key);
	}

	@Override
	@Deprecated
	void deleteLine(final String key);
}
