package org.firstinspires.ftc.teamcode.eventloop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class OverclockOpMode extends LinearOpMode {
	private OverclockMode overclockMode = OverclockMode.SUPER_LINEAR;

	@Override
	public void runOpMode() throws InterruptedException {
		try {
			op_init();

			while (opModeInInit()) {
				loop_init();
			}

			if (isStopRequested()) {
				op_end();
				return;
			}

			op_start();

			while (opModeIsActive()) {
				overclockMode.newLoop(this::op_loop).run();
			}
		} catch (final Throwable e) {
			exception_entry(e);
		} finally {
			op_end();
		}
	}

	public void setOverclockMode(final OverclockMode overclockMode) {
		setMode(overclockMode, 0L);
	}

	public void setMode(final OverclockMode mode, final long utilConfigureValue) {
		this.overclockMode = mode;
		this.overclockMode.utilConfigureValue.set(utilConfigureValue);
	}

	public abstract void op_init();

	public void loop_init() {
		idle();
	}

	public void op_start() {
		idle();
	}

	public abstract void op_loop();

	public void op_end() {
		idle();
	}

	public void exception_entry(final Throwable e) {
		throw new RuntimeException(e);
	}
}
