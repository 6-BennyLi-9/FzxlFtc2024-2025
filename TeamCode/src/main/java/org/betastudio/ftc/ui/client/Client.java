package org.betastudio.ftc.ui.client;

import androidx.annotation.NonNull;

import org.betastudio.ftc.ui.log.FtcLogTunnel;
import org.betastudio.ftc.Interfaces;
import org.betastudio.ftc.util.message.TelemetryMsg;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * @noinspection UnusedReturnValue
 */
public interface Client extends Interfaces.MessagesProcessRequired <TelemetryMsg>, Interfaces.Updatable {
	void clear();

	/// 注意：这是新的Data
	void putData(final String key, final String val);
	/// @throws RuntimeException 如果未能找到key所指向的值，将会抛出异常
	void deleteData(final String key);
	/// 自动创建新的行如果key所指向的值不存在
	void changeData(final String key, final String val);

	/// 注意：这是新的Line
	void putLine(final String key);
	/// @throws RuntimeException 如果未能找到key所指向的值，将会抛出异常
	void deleteLine(final String key);
	/**
	 * 将key行替代为val，自动创建新的行如果key所指向的值不存在
	 *
	 * @param oldData 目标行
	 * @param newData 替换行
	 */
	@Deprecated
	void changeLine(final String oldData, final String newData);

	void speak(String text, String languageCode, String countryCode);

	default void configViewMode(final ClientViewMode clientViewMode) {
		ClientViewMode.globalViewMode = clientViewMode;
	}

	UpdateConfig getUpdateConfig();

	void setUpdateConfig(final UpdateConfig updateConfig);

	default ClientViewMode getCurrentViewMode() {
		return ClientViewMode.globalViewMode;
	}

	Telemetry getOriginTelemetry();


	//------------------------
	// DEFAULT IMPLEMENTATION
	//------------------------
	default void switchViewMode() {
		switch (getCurrentViewMode()) {
			case ORIGIN_TELEMETRY:
				ClientViewMode.globalViewMode = ClientViewMode.FTC_LOG;
				break;
			case FTC_LOG:
				ClientViewMode.globalViewMode = ClientViewMode.THREAD_SERVICE;
				break;
			case THREAD_SERVICE:
			default:
				ClientViewMode.globalViewMode = ClientViewMode.ORIGIN_TELEMETRY;
				break;
		}
	}

	/// 注意：这是新的Data
	default void putData(final String key, @NonNull final Object val) {
		putData(key, val.toString());
	}
	/// 自动创建新的行如果key所指向的值不存在
	default void changeData(final String key, @NonNull final Object val) {
		changeData(key, val.toString());
	}
	/// 注意：这是新的Line
	default void putLine(@NonNull final Object key) {
		putLine(key.toString());
	}

	default void speak(final String text) {
		speak(text, null, null);
	}

	FtcLogTunnel getTargetLogTunnel();
}
