package org.firstinspires.ftc.teamcode;

/**
 * 一个非常好用的计数器
 */
public final class TickEncoder {
	private int ticked;

	public void tickRst() {
		ticked = 0;
	}

	public void tick() {
		++ ticked;
	}

	public void modTicked(final int mod) {
		ticked %= mod;
	}

	public void tickAndMod(final int mod) {
		tick();
		modTicked(mod);
	}

	public int getTicked() {
		return ticked;
	}

	public void setTicked(final int ticked) {
		this.ticked = ticked;
	}
}
