package org.firstinspires.ftc.opmodes.tests;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Autonomous(group = "9_Tests")
public final class SampleLocalExceptionHandle extends LinearOpMode implements Thread.UncaughtExceptionHandler {
	private Throwable e;

	@Override
	public void runOpMode() throws InterruptedException {
		telemetry.setAutoClear(false);
		Thread test=new Thread(
				()->{
					Telemetry.Item item =telemetry.addData("i",0);
					for (int i = 0 ; i < 100 ; i++) {
						item.setValue(i);
						telemetry.update();
						sleep(100);
						if (i==50){
							throw new RuntimeException();
						}
					}
				}
		);

		test.setUncaughtExceptionHandler(this);

		test.start();

		waitForStart();

		while (opModeIsActive()){
			if (e != null){
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
		this.e=e;
	}
}
