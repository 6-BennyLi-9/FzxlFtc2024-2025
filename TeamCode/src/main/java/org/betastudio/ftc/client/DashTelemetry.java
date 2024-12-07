package org.betastudio.ftc.client;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DashTelemetry implements Telemetry {
	private final MultipleTelemetry telemetries;
	private final FtcDashboard      dashboard;
	private       TelemetryPacket   packet = new TelemetryPacket();

	public DashTelemetry(@NonNull final FtcDashboard dashboard, final Telemetry... telemetries) {
		this.telemetries = new MultipleTelemetry(telemetries);
		this.dashboard = dashboard;
//		if (! Arrays.asList(telemetries).contains(dashboard.getTelemetry())) {
//			this.telemetries.addTelemetry(dashboard.getTelemetry());
//		}
		this.telemetries.clearAll();
	}

	@Override
	public Item addData(final String caption, final String format, final Object... args) {
		packet.put(caption, String.format(format, args));
		return telemetries.addData(caption, format, args);
	}

	@Override
	public Item addData(final String caption, final Object value) {
		packet.put(caption, value);
		return telemetries.addData(caption, value);
	}

	@Override
	public <T> Item addData(final String caption, final Func <T> valueProducer) {
		packet.put(caption, valueProducer);
		return telemetries.addData(caption, valueProducer);
	}

	@Override
	public <T> Item addData(final String caption, final String format, final Func <T> valueProducer) {
		packet.put(caption, String.format(format, valueProducer));
		return telemetries.addData(caption, format, valueProducer);
	}

	/**
	 * 不受 {@link FtcDashboard} 支持
	 */
	@Override
	public boolean removeItem(final Item item) {
		return telemetries.removeItem(item);
	}

	/**
	 * 不受 {@link FtcDashboard} 支持
	 */
	@Override
	public void clear() {
		telemetries.clear();
	}

	/**
	 * 不受 {@link FtcDashboard} 支持
	 */
	@Override
	public void clearAll() {
		telemetries.clearAll();
	}

	/**
	 * 不受 {@link FtcDashboard} 支持
	 */
	@Override
	public Object addAction(final Runnable action) {
		return telemetries.addAction(action);
	}

	/**
	 * 不受 {@link FtcDashboard} 支持
	 */
	@Override
	public boolean removeAction(final Object token) {
		return telemetries.removeAction(token);
	}

	/**
	 * 不受 {@link FtcDashboard} 支持
	 */
	@Override
	public void speak(final String text) {
		telemetries.speak(text);
	}

	/**
	 * 不受 {@link FtcDashboard} 支持
	 */
	@Override
	public void speak(final String text, final String languageCode, final String countryCode) {
		telemetries.speak(text, languageCode, countryCode);
	}

	@Override
	public boolean update() {
		dashboard.sendTelemetryPacket(packet);
		packet = new TelemetryPacket();
		return telemetries.update();
	}

	@Override
	public Line addLine() {
		packet.addLine("");
		return telemetries.addLine();
	}

	@Override
	public Line addLine(final String lineCaption) {
		packet.addLine(lineCaption);
		return telemetries.addLine(lineCaption);
	}

	/**
	 * 不受 {@link FtcDashboard} 支持
	 */
	@Override
	public boolean removeLine(final Line line) {
		return telemetries.removeLine(line);
	}

	/**
	 * 不受 {@link FtcDashboard} 支持
	 */
	@Override
	public boolean isAutoClear() {
		return telemetries.isAutoClear();
	}

	/**
	 * 不受 {@link FtcDashboard} 支持
	 */
	@Override
	public void setAutoClear(final boolean autoClear) {
		telemetries.setAutoClear(autoClear);
	}

	/**
	 * 不受 {@link FtcDashboard} 支持
	 */
	@Override
	public int getMsTransmissionInterval() {
		return telemetries.getMsTransmissionInterval();
	}

	/**
	 * 不受 {@link FtcDashboard} 支持
	 */
	@Override
	public void setMsTransmissionInterval(final int msTransmissionInterval) {
		telemetries.setMsTransmissionInterval(msTransmissionInterval);
	}

	/**
	 * 不受 {@link FtcDashboard} 支持
	 */
	@Override
	public String getItemSeparator() {
		return telemetries.getItemSeparator();
	}

	/**
	 * 不受 {@link FtcDashboard} 支持
	 */
	@Override
	public void setItemSeparator(final String itemSeparator) {
		telemetries.setItemSeparator(itemSeparator);
	}

	/**
	 * 不受 {@link FtcDashboard} 支持
	 */
	@Override
	public String getCaptionValueSeparator() {
		return telemetries.getCaptionValueSeparator();
	}

	/**
	 * 不受 {@link FtcDashboard} 支持
	 */
	@Override
	public void setCaptionValueSeparator(final String captionValueSeparator) {
		telemetries.setCaptionValueSeparator(captionValueSeparator);
	}

	/**
	 * 不受 {@link FtcDashboard} 支持
	 */
	@Override
	public void setDisplayFormat(final DisplayFormat displayFormat) {
		telemetries.setDisplayFormat(displayFormat);
	}

	/**
	 * 不受 {@link FtcDashboard} 支持
	 */
	@Override
	public Log log() {
		return telemetries.log();
	}

//	public void addPacketVal(final String caption,final Object value){
//		packet.put(caption,value);
//	}
//	public void addPacketVal(Map <String,Object> container){
//		packet.putAll(container);
//	}
//
//	public void addLineToTelemetry(String line){
//		telemetries.addLine(line);
//	}

	public void addSmartLine(final String capital, final Object value) {
		if (value != null) {
			telemetries.addData(capital, value);
			packet.put(capital, value);
		} else {
			telemetries.addLine(capital);
			packet.addLine(capital);
		}
	}
}
