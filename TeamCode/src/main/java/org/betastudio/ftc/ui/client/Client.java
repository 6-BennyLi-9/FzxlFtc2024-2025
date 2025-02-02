package org.betastudio.ftc.ui.client;

import androidx.annotation.NonNull;

import org.betastudio.ftc.util.entry.MessagesProcessRequired;
import org.betastudio.ftc.util.message.TelemetryMsg;
import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * @noinspection UnusedReturnValue
 */
public interface Client extends MessagesProcessRequired <TelemetryMsg> , Telemetry {

	Client putData(final String key, final String val);

	Client deleteData(final String key);

	Client changeData(final String key, final String val);

	Client putLine(final String key);

	Client deleteLine(final String key);

	Client changeLine(final String oldData, final String newData);

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

	/**
	 *
	 * @param caption   the caption to use
	 * @param format    the string by which the arguments are to be formatted
	 * @param args      the arguments to format
	 */
	@Override
	default Item addData(final String caption, final String format, final Object... args) {
		putData(caption, String.format(format, args));
		return null;
	}

	/**
	 *
	 * @param caption   the caption to use
	 * @param value     the value to display
	 */
	@Override
	default Item addData(final String caption, final Object value) {
		putData(caption, value);
		return null;
	}

	/**
	 *
	 * @param caption           the caption to use
	 * @param valueProducer     the object which will provide the value to display
	 */
	@Override
	default <T> Item addData(final String caption, @NonNull final Func <T> valueProducer) {
		putData(caption, valueProducer.value());
		return null;
	}

	/**
	 *
	 * @param caption           the caption to use
	 * @param valueProducer     the object which will provide the value to display
	 */
	@Override
	default <T> Item addData(final String caption, final String format, @NonNull final Func <T> valueProducer) {
		putData(caption, String.format(format, valueProducer.value()));
		return null;
	}

	@Override
	default void clearAll() {
		clear();
	}


	@Override
	default Line addLine() {
		putLine("");
		return null;
	}

	/**
	 *
	 * @param lineCaption the caption for the line
	 */
	@Override
	default Line addLine(final String lineCaption) {
		putLine(lineCaption);
		return null;
	}

	@Override
	default boolean isAutoClear() {
		return false;
	}

//-----------------------------
// DEFAULT UNSUPPORTED METHODS
//-----------------------------

	/**
	 * @param item  the item to remove
	 * @throws UnsupportedOperationException 默认不支持此方法
	 */
	@Override
	default boolean removeItem(final Item item) {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 * @param line the line to be removed
	 * @throws UnsupportedOperationException 默认不支持此方法
	 */
	@Override
	default boolean removeLine(final Line line) {
		throw new UnsupportedOperationException();
	}


	/**
	 * @param autoClear if true, {@link #clear()} is automatically called after each call to {@link #update()}.
	 * @throws UnsupportedOperationException 默认不支持此方法
	 */
	@Override
	default void setAutoClear(final boolean autoClear) {
		throw new UnsupportedOperationException();
	}


	/**
	 * @throws UnsupportedOperationException 默认不支持此方法
	 */
	@Override
	default int getMsTransmissionInterval() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param msTransmissionInterval  the minimum interval between {@link Telemetry} transmissions
	 *                                from the robot controller to the driver station
	 * @throws UnsupportedOperationException 默认不支持此方法
	 */
	@Override
	default void setMsTransmissionInterval(final int msTransmissionInterval) {
		throw new UnsupportedOperationException();
	}


	/**
	 * @throws UnsupportedOperationException 默认不支持此方法
	 */
	@Override
	default String getItemSeparator() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException 默认不支持此方法
	 */
	@Override
	default void setItemSeparator(final String itemSeparator) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException 默认不支持此方法
	 */
	@Override
	default String getCaptionValueSeparator() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException 默认不支持此方法
	 */
	@Override
	default void setCaptionValueSeparator(final String captionValueSeparator) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param displayFormat the telemetry display format the Driver Station should use
	 * @throws UnsupportedOperationException 默认不支持此方法
	 */
	@Override
	default void setDisplayFormat(final DisplayFormat displayFormat) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param minDecimalPlaces - the minimum number of places to show when Double or Float is passed in without a Format
	 * @param maxDecimalPlaces - the maximum number of places to show when Double or Float is passed in without a Format
	 * @throws UnsupportedOperationException 默认不支持此方法
	 */
	@Override
	default void setNumDecimalPlaces(final int minDecimalPlaces, final int maxDecimalPlaces) {
		throw new UnsupportedOperationException();
	}


	/**
	 * @throws UnsupportedOperationException 默认不支持此方法
	 */
	@Override
	default Log log() {
		throw new UnsupportedOperationException();
	}
}
