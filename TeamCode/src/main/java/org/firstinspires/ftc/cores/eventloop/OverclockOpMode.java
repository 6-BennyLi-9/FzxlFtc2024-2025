package org.firstinspires.ftc.cores.eventloop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class OverclockOpMode extends LinearOpMode {
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
				op_loop();
			}
		} catch (final Throwable e) {
			exception_entry(e);
		} finally {
			op_end();
		}
	}

	public abstract void op_init();

	public void loop_init() {
	}

	public void op_start() {
	}

	public abstract void op_loop();

	public void op_end() {
	}

	public void exception_entry(final Throwable e) {
	}
}
