package org.firstinspires.ftc.opmodes.tests;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.betastudio.ftc.Annotations;
import org.firstinspires.ftc.robotcore.external.Telemetry;

@Autonomous(group = "9_Tests")
@Annotations.TestDoneSuccessfully
public final class SampleLocalExceptionHandle extends LinearOpMode implements Thread.UncaughtExceptionHandler {
	private Throwable e;

	@Override
	public void runOpMode() throws InterruptedException {
		telemetry.setAutoClear(false);
		final Thread test = new Thread(() -> {
			final Telemetry.Item item = telemetry.addData("i", 0);
			for (int i = 0 ; 100 > i ; i++) {
				item.setValue(i);
				telemetry.update();
				sleep(100);
				if (50 == i) {
					throw new RuntimeException();
				}
			}
		});

		test.setUncaughtExceptionHandler(this);

		test.start();

		waitForStart();

		while (opModeIsActive()) {
			if (null != e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void uncaughtException(@NonNull final Thread t, @NonNull final Throwable e) {
		this.e = e;
	}
}
