package org.betastudio.ftc.ui.client;

public enum ClientViewMode {
	FTC_LOG, ORIGIN_TELEMETRY, THREAD_SERVICE;
	public static ClientViewMode globalViewMode = ORIGIN_TELEMETRY;
}
