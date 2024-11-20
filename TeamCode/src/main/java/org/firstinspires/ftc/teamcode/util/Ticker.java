package org.firstinspires.ftc.teamcode.util;

public class Ticker {
	private long ticked;

	public void tickRst() {
		ticked = 0;
	}

	public void tick() {
		++ticked;
	}

	public void modeTicked(final long mod) {
		ticked %= mod;
	}
}
