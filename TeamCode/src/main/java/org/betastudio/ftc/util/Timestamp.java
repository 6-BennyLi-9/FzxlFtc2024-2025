package org.betastudio.ftc.util;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Timer;

public class Timestamp {
	private final String minute;
	private final String second;

	public Timestamp(){
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
	protected void checkAccess(){
		assert 2 >= second.length();
		assert Double.valueOf(minute).doubleValue()==Integer.valueOf(minute).intValue();
	}

	@NonNull
	@Override
	public String toString() {
		return minute + ":" + second;
	}

	public String getMinute() {
		return minute;
	}

	public String getSecond() {
		return second;
	}
}
