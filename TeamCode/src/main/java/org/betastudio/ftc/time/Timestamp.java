package org.betastudio.ftc.time;

import androidx.annotation.NonNull;

public class Timestamp {
	private final String minute;
	private final String second;

	public Timestamp() {
		final double t      = Timer.getCurrentTime();
		final int    minute = (int) t / 60;
		final int    second = (int) t % 60;
		this.minute = String.valueOf(minute);
		this.second = String.valueOf(second);
	}

	public Timestamp(final String minute, final String second) {
		this.minute = minute;
		this.second = second;
	}

	/**
	 * 需要测试
	 */
	protected void checkAccess() {
		assert 2 >= second.length();
		assert Double.valueOf(minute).doubleValue() == Integer.valueOf(minute).intValue();
	}

	@NonNull
	@Override
	public String toString() {
		if (2 < minute.length()) {
			return ".." + minute.substring(minute.length() - 3) + ":" + second;
		} else {
			return minute + ":" + second;
		}
	}

	public String getMinute() {
		return minute;
	}

	public String getSecond() {
		return second;
	}

	public int toSecondsInt() {
		return Integer.parseInt(minute) * 60 + Integer.parseInt(second);
	}
}
