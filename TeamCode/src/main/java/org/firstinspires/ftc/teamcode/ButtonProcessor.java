package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

/**
 * 用于控制按键布尔状态，更加轻量化
 */
public final class ButtonProcessor {
	public enum ButtonConfig {
		WhilePressing, SingleWhenPressed
	}

	public final ButtonConfig config;
	public final TickEncoder smartCounter;

	private boolean lst, now;

	public ButtonProcessor(final ButtonConfig config) {
		this.config = config;
		smartCounter = new TickEncoder();
	}

	public void sync(final boolean input) {
		switch (config) {
			case WhilePressing:
				now = input;
				break;
			case SingleWhenPressed:
			default:
				lst = now;
				now = input;
				break;
		}
	}

	public boolean getEnabled() {
		switch (config) {
			case WhilePressing:
				return now;
			case SingleWhenPressed:
			default:
				return now && ! lst;
		}
	}

	@NonNull
	@Override
	public String toString() {
		return "lst:" + lst + ",now:" + now;
	}
}
