package org.betastudio.ftc.util;

import androidx.annotation.NonNull;

import java.util.Locale;

public final class RuntimeMemoryStamp {
	private static final double BYTE_TO_MB = 1048576f;
	private final double usedMemory;
	private final double freeMemory;
	private final double totalMemory;
	private final double memoryRatio;

	public RuntimeMemoryStamp() {
		Runtime runtime = Runtime.getRuntime();
		totalMemory = runtime.totalMemory() / BYTE_TO_MB;
		freeMemory = runtime.freeMemory() / BYTE_TO_MB;
		usedMemory = totalMemory - freeMemory;
		memoryRatio = usedMemory / totalMemory;
	}


	public double getFreeMemory() {
		return freeMemory;
	}

	public double getMemoryRatio() {
		return memoryRatio;
	}

	public double getTotalMemory() {
		return totalMemory;
	}

	public double getUsedMemory() {
		return usedMemory;
	}

	@NonNull
	@Override
	public String toString() {
		return String.format(Locale.SIMPLIFIED_CHINESE, "%.2f/%.2f(%.1f%%)",usedMemory,totalMemory,memoryRatio * 100);
	}
}
